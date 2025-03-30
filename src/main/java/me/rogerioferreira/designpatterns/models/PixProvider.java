package me.rogerioferreira.designpatterns.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pix_providers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PixProvider {
  @Id
  String id;
  String name;
  double minFee;
  double maxFee;
  double minSalesVolume;
  double maxSalesVolume;
}
