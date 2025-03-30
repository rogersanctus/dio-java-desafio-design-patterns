package me.rogerioferreira.designpatterns.services;

import org.springframework.stereotype.Service;

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
  public boolean isPaymentCompleted(String paymentOrderId) {
    return true;
  }

}
