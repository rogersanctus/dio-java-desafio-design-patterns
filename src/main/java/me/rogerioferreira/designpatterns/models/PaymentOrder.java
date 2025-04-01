package me.rogerioferreira.designpatterns.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.rogerioferreira.designpatterns.enums.PaymentStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrder {
  @Id
  private String id;

  private String providerPaymentId;
  private String providerId;

  private double amount;
  private double fee;
  private PaymentStatus status;
}
