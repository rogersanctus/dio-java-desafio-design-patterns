package me.rogerioferreira.designpatterns.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.services.PixProviderService;

@RestController
@RequestMapping("/payments")
public class PaymentController {
  @Autowired
  private PixProviderService pixProviderService;

  @GetMapping("/fee-per-sales-volume/{salesVolume}")
  public ResponseEntity<Double> calculateFeePerSalesVolume(@RequestParam double salesVolume) {
    PixProvider pixProvider = new PixProvider("123", "Fintech Pague Mais", 0.8, 1.9, 100000, 500000);

    var fee = pixProviderService.interpolateSalesAndFees(pixProvider, salesVolume);

    return ResponseEntity.ok(fee);
  }
}
