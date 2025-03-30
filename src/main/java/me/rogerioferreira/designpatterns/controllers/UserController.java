package me.rogerioferreira.designpatterns.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@RequestParam String id) {
    var mayBeUser = userRepository.findById(id);

    if (mayBeUser.isPresent()) {
      return ResponseEntity.ok(mayBeUser.get());
    }

    return ResponseEntity.notFound().build();
  }

  @PostMapping()
  public ResponseEntity<User> save(@RequestBody User user) {
    var savedUser = userRepository.save(user);

    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedUser.getId())
        .toUri();

    return ResponseEntity.created(uri).body(savedUser);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> update(@RequestParam String id, @RequestBody User user) {
    var mayBeUser = userRepository.findById(id);

    if (!mayBeUser.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    user.setId(id);

    return ResponseEntity.ok(userRepository.save(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@RequestParam String id) {
    var mayBeUser = userRepository.findById(id);

    if (!mayBeUser.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    userRepository.deleteById(id);

    return ResponseEntity.noContent().build();
  }
}
