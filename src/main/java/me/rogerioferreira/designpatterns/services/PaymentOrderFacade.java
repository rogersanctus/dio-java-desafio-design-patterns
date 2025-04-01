package me.rogerioferreira.designpatterns.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.rogerioferreira.designpatterns.dtos.PaymentOrderResultDto;
import me.rogerioferreira.designpatterns.enums.PaymentStatus;
import me.rogerioferreira.designpatterns.models.PaymentOrder;
import me.rogerioferreira.designpatterns.repositories.PaymentOrderRepository;
import me.rogerioferreira.designpatterns.repositories.UserRepository;
import me.rogerioferreira.designpatterns.utils.CurrencyUtils;

@Component
public class PaymentOrderFacade {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PixProviderService pixProviderService;

  @Autowired
  private PaymentOrderRepository paymentOrderRepository;

  public PaymentOrderResultDto createPaymentOrder(String userId, double amount)
      throws RuntimeException {
    var mayBeUser = userRepository.findById(userId);

    if (!mayBeUser.isPresent()) {
      throw new RuntimeException("User not found");
    }

    var user = mayBeUser.get();

    var salesVolume = user.getAverageSalesVolume();
    var pixProviderWithFee = pixProviderService.getPixProviderWithFee(salesVolume);
    var pixApiProvider = pixProviderService.getApiProvider(pixProviderWithFee.pixProvider());

    if (pixApiProvider == null) {
      throw new RuntimeException("No pix api provider found for this user");
    }

    var internalPaymentOrderId = UUID.randomUUID().toString();

    var providerPaymentOrderId = pixApiProvider.createPaymentOrder(internalPaymentOrderId,
        amount);

    var internalPaymentOrder = new PaymentOrder(internalPaymentOrderId, providerPaymentOrderId,
        pixProviderWithFee.pixProvider().getId(), amount,
        pixProviderWithFee.fee(), PaymentStatus.CREATED);

    var qrCode = pixApiProvider.getQrCode(providerPaymentOrderId);
    var totalToReceive = amount * (1 - pixProviderWithFee.fee() / 100);

    paymentOrderRepository.save(internalPaymentOrder);

    return new PaymentOrderResultDto(internalPaymentOrderId, qrCode, pixProviderWithFee.fee(),
        CurrencyUtils.round(totalToReceive, 2));
  }
}
