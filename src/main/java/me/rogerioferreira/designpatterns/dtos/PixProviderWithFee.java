package me.rogerioferreira.designpatterns.dtos;

import me.rogerioferreira.designpatterns.models.PixProvider;

public record PixProviderWithFee(PixProvider pixProvider, double fee) {
}
