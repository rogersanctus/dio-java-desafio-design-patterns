package me.rogerioferreira.designpatterns.dtos;

public record PaymentOrderResultDto(
    String paymentOrderId,
    String qrCode,
    double fee,
    double totalToReceive) {
}
