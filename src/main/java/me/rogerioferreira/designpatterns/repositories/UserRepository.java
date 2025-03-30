package me.rogerioferreira.designpatterns.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import me.rogerioferreira.designpatterns.models.User;

public interface UserRepository extends JpaRepository<User, String> {
}
