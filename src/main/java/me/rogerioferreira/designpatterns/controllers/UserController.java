package me.rogerioferreira.designpatterns.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.rogerioferreira.designpatterns.models.User;
import me.rogerioferreira.designpatterns.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @GetMapping()
  public List<User> findAll() {
    return userRepository.findAll();
  }
}
