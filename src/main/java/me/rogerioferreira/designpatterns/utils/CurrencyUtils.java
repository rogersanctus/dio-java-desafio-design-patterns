package me.rogerioferreira.designpatterns.utils;

public class CurrencyUtils {
  public static double round(double value, int places) {
    double scale = Math.pow(10, places);
    return Math.round(value * scale) / scale;
  }
}
