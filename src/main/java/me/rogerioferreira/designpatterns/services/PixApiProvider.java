package me.rogerioferreira.designpatterns.services;

public interface PixApiProvider {
  String createPaymentOrder(String externalId, double amount);

  String getQrCode(String paymentOrderId);

  boolean isPaymentCompleted(String paymentOrderId);
}
