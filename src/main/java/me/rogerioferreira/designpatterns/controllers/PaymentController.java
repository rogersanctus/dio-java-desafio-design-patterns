package me.rogerioferreira.designpatterns.controllers;

import java.util.HashMap;

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
import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.UserRepository;
import me.rogerioferreira.designpatterns.services.PixProviderService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Autowired
  private PixProviderService pixProviderService;

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
    var pixApiProvider = pixProviderService.getApiProvider(pixProviderWithFee);

    if (pixApiProvider == null) {
      return ResponseEntity.badRequest().body(new HashMap<String, Object>() {
        {
          put("message", "No pix api provider found for this user");
        }
      });
    }

    var paymentOrderId = pixApiProvider.createPaymentOrder(paymentOrderCreationDTO.externalId(),
        paymentOrderCreationDTO.amount());

    var qrCode = pixApiProvider.getQrCode(paymentOrderId);
    var totalToReceive = paymentOrderCreationDTO.amount() * (1 - pixProviderWithFee.fee() / 100);

    return ResponseEntity.ok(new PaymentOrderResultDto(
        paymentOrderId,
        qrCode,
        pixProviderWithFee.fee(),
        Math.round(totalToReceive * 100.0) / 100.0));
  }

  @GetMapping("/completed/{paymentOrderId}")
  public ResponseEntity<?> isPaymentCompleted(@RequestParam String paymentOrderId) {
    return ResponseEntity.ok("TODO");
  }
}
