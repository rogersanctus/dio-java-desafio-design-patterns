package me.rogerioferreira.designpatterns.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import me.rogerioferreira.designpatterns.models.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, String> {
}
