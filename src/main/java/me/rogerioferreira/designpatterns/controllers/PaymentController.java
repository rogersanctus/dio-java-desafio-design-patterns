package me.rogerioferreira.designpatterns.controllers;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.rogerioferreira.designpatterns.dtos.PaymentOrderCreationDto;
import me.rogerioferreira.designpatterns.dtos.PaymentOrderResultDto;
import me.rogerioferreira.designpatterns.dtos.PaymentOrderStatus;
import me.rogerioferreira.designpatterns.enums.PaymentStatus;
import me.rogerioferreira.designpatterns.models.PaymentOrder;
import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PaymentOrderRepository;
import me.rogerioferreira.designpatterns.repositories.UserRepository;
import me.rogerioferreira.designpatterns.services.PixProviderService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Autowired
  private PixProviderService pixProviderService;

  @Autowired
  private PaymentOrderRepository paymentOrderRepository;

  @Autowired
  private UserRepository userRepository;

  @GetMapping("/fee-per-sales-volume/{salesVolume}")
  public ResponseEntity<Double> calculateFeePerSalesVolume(@RequestParam double salesVolume) {
    PixProvider pixProvider = new PixProvider("123", "Fintech Pague Mais", 0.8, 1.9, 100000, 500000);

    var fee = pixProviderService.interpolateSalesAndFees(pixProvider, salesVolume);

    return ResponseEntity.ok(fee);
  }

  @PostMapping()
  public ResponseEntity<?> createPayment(@RequestBody PaymentOrderCreationDto paymentOrderCreationDTO) {
    var mayBeUser = userRepository.findById(paymentOrderCreationDTO.userId());

    if (!mayBeUser.isPresent()) {
      return ResponseEntity.badRequest().body(new HashMap<String, Object>() {
        {
          put("message", "User not found");
        }
      });
    }

    var user = mayBeUser.get();

    var salesVolume = user.getAverageSalesVolume();
    var pixProviderWithFee = pixProviderService.getPixProviderWithFee(salesVolume);
    var pixApiProvider = pixProviderService.getApiProvider(pixProviderWithFee.pixProvider());

    if (pixApiProvider == null) {
      return ResponseEntity.badRequest().body(new HashMap<String, Object>() {
        {
          put("message", "No pix api provider found for this user");
        }
      });
    }

    var internalPaymentOrderId = UUID.randomUUID().toString();

    var providerPaymentOrderId = pixApiProvider.createPaymentOrder(internalPaymentOrderId,
        paymentOrderCreationDTO.amount());

    var internalPaymentOrder = new PaymentOrder(internalPaymentOrderId, providerPaymentOrderId,
        pixProviderWithFee.pixProvider().getId(), paymentOrderCreationDTO.amount(),
        pixProviderWithFee.fee(), PaymentStatus.CREATED);

    var qrCode = pixApiProvider.getQrCode(providerPaymentOrderId);
    var totalToReceive = paymentOrderCreationDTO.amount() * (1 - pixProviderWithFee.fee() / 100);

    paymentOrderRepository.save(internalPaymentOrder);

    return ResponseEntity.ok(new PaymentOrderResultDto(
        internalPaymentOrderId,
        qrCode,
        pixProviderWithFee.fee(),
        Math.round(totalToReceive * 100.0) / 100.0));
  }

  @GetMapping("/status/{paymentOrderId}")
  public ResponseEntity<?> getStatus(@RequestParam String paymentOrderId) {
    var mayBePaymentOrder = this.paymentOrderRepository.findById(paymentOrderId);

    if (!mayBePaymentOrder.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    var paymentOrder = mayBePaymentOrder.get();

    var mayBePixProvider = pixProviderService.getProviderById(paymentOrder.getProviderId());

    if (!mayBePixProvider.isPresent()) {
      return ResponseEntity.internalServerError().body(new HashMap<>() {
        {
          put("message", "Provider not found for this payment order");
        }
      });
    }

    var pixProvider = mayBePixProvider.get();
    var pixApiProvider = pixProviderService.getApiProvider(pixProvider);

    var paymentStatus = pixApiProvider.getStatus(paymentOrderId);

    return ResponseEntity.ok(new PaymentOrderStatus(paymentOrderId, paymentStatus));
  }
}
