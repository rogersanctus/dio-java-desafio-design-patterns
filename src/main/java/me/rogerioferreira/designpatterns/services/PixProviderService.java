package me.rogerioferreira.designpatterns.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.rogerioferreira.designpatterns.dtos.PixProviderWithFee;
import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PixProviderRepository;

@Component
public class PixProviderService {

  @Autowired
  private PixProviderRepository pixProviderRepository;

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

  public Optional<PixProvider> getProviderById(String id) {
    return pixProviderRepository.findById(id);
  }

  public PixProviderWithFee getPixProviderWithFee(double salesVolume) {
    return pixProviderRepository
        .findAll()
        .stream()
        .map(pixProvider -> new PixProviderWithFee(pixProvider, interpolateSalesAndFees(pixProvider, salesVolume)))
        .sorted((a, b) -> Double.compare(
            a.fee(),
            b.fee()))
        .findFirst()
        .orElse(null);
  }

  public PixApiProvider getApiProvider(PixProvider pixProvider) {
    if (pixProvider == null) {
      return null;
    }

    return switch (pixProvider.getName()) {
      case "DasQuantas" -> new FintechDasQuantasPixProvider();
      case "PagueMais" -> new FintechPagueMaisPixProvider();
      default -> null;
    };
  }
}
