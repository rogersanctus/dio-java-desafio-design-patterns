package me.rogerioferreira.designpatterns.dtos;

import me.rogerioferreira.designpatterns.enums.PaymentStatus;

public record PaymentOrderStatus(
    String paymentOrderId,
    PaymentStatus status) {
}
