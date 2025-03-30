package me.rogerioferreira.designpatterns.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import me.rogerioferreira.designpatterns.models.PixProvider;

public interface PixProviderRepository extends JpaRepository<PixProvider, String> {
}
