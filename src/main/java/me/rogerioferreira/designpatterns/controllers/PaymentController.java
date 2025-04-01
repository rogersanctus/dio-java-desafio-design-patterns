package me.rogerioferreira.designpatterns.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.rogerioferreira.designpatterns.dtos.ErrorMessage;
import me.rogerioferreira.designpatterns.dtos.PaymentOrderCreationDto;
import me.rogerioferreira.designpatterns.dtos.PaymentOrderStatus;
import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PaymentOrderRepository;
import me.rogerioferreira.designpatterns.services.PaymentOrderFacade;
import me.rogerioferreira.designpatterns.services.PixProviderService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Autowired
  private PixProviderService pixProviderService;

  @Autowired
  private PaymentOrderRepository paymentOrderRepository;

  @Autowired
  private PaymentOrderFacade paymentOrderFacade;

  @GetMapping("/fee-per-sales-volume/{salesVolume}")
  public ResponseEntity<Double> calculateFeePerSalesVolume(@RequestParam double salesVolume) {
    PixProvider pixProvider = new PixProvider("123", "Fintech Pague Mais", 0.8, 1.9, 100000, 500000);

    var fee = pixProviderService.interpolateSalesAndFees(pixProvider, salesVolume);

    return ResponseEntity.ok(fee);
  }

  @PostMapping()
  public ResponseEntity<?> createPayment(@RequestBody PaymentOrderCreationDto paymentOrderCreationDTO) {
    var paymentOrder = this.paymentOrderFacade.createPaymentOrder(
        paymentOrderCreationDTO.userId(),
        paymentOrderCreationDTO.amount());

    return ResponseEntity.ok(paymentOrder);
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
      return ResponseEntity.internalServerError().body(new ErrorMessage("Provider not found for this payment order"));
    }

    var pixProvider = mayBePixProvider.get();
    var pixApiProvider = pixProviderService.getApiProvider(pixProvider);

    var paymentStatus = pixApiProvider.getStatus(paymentOrderId);

    return ResponseEntity.ok(new PaymentOrderStatus(paymentOrderId, paymentStatus));
  }
}
