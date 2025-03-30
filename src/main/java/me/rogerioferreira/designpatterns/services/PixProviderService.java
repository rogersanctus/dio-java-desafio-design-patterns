package me.rogerioferreira.designpatterns.services;

import org.springframework.stereotype.Component;

import me.rogerioferreira.designpatterns.models.PixProvider;

@Component
public class PixProviderService {
  public double interpolateSalesAndFees(PixProvider pixProvider, double salesVolume) {
    salesVolume = Math.min(salesVolume, pixProvider.getMaxSalesVolume()); // Limita o volume de vendas ao máximo do
    // provider
    salesVolume = Math.max(salesVolume, pixProvider.getMinSalesVolume()); // E ao mínimo

    var feesDifference = pixProvider.getMaxFee() - pixProvider.getMinFee();
    var salesDifference = pixProvider.getMinSalesVolume() - pixProvider.getMaxSalesVolume(); // Inversamente
                                                                                             // proporcional

    var salesToMaxDifference = salesVolume - pixProvider.getMaxSalesVolume();
    var minFeeSalesDifferenceProduct = pixProvider.getMinFee() * salesDifference;
    var interpolatedFee = feesDifference * (salesToMaxDifference) + minFeeSalesDifferenceProduct;
    interpolatedFee /= salesDifference;

    return interpolatedFee;
  }
}
