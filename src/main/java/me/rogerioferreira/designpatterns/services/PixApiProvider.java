package me.rogerioferreira.designpatterns.services;

import me.rogerioferreira.designpatterns.enums.PaymentStatus;

public interface PixApiProvider {
  String createPaymentOrder(String externalId, double amount);

  String getQrCode(String paymentOrderId);

  PaymentStatus getStatus(String paymentOrderId);
}
