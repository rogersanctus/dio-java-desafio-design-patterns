package me.rogerioferreira.designpatterns.services;

import org.springframework.stereotype.Service;

import me.rogerioferreira.designpatterns.enums.PaymentStatus;

@Service
public class FintechDasQuantasPixProvider implements PixApiProvider {
  @Override
  public String createPaymentOrder(String externalId, double amount) {
    return "DQS-12345cfee-22-9988";
  }

  @Override
  public String getQrCode(String paymentOrderId) {
    return "Finge  que\nestá vendo\num QR Code\nda Fintech\nDas Quantas";
  }

  @Override
  public PaymentStatus getStatus(String paymentOrderId) {
    return PaymentStatus.COMPLETED;
  }
}
