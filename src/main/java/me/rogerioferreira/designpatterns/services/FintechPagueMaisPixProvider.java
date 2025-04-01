package me.rogerioferreira.designpatterns.services;

import org.springframework.stereotype.Service;

import me.rogerioferreira.designpatterns.enums.PaymentStatus;

@Service
public class FintechPagueMaisPixProvider implements PixApiProvider {
  @Override
  public String createPaymentOrder(String externalId, double amount) {
    return "PG+-12345abff-12-1234";
  }

  @Override
  public String getQrCode(String paymentOrderId) {
    return "Finge  que\nestá vendo\num QR Code\nda Fintech\nPague Mais";
  }

  @Override
  public PaymentStatus getStatus(String paymentOrderId) {
    return PaymentStatus.PENDING;
  }
}
