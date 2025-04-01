package me.rogerioferreira.designpatterns.dtos;

public record PaymentOrderCreationDto(
    String userId,
    double amount) {
}
