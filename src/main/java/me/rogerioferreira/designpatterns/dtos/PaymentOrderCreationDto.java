package me.rogerioferreira.designpatterns.dtos;

public record PaymentOrderCreationDto(
    String userId,
    String externalId,
    double amount) {
}
