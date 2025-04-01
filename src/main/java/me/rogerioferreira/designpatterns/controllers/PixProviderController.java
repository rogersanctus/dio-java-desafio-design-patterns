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

import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PixProviderRepository;
import me.rogerioferreira.designpatterns.services.PixProviderService;

@RestController
@RequestMapping("/pix-providers")
public class PixProviderController {
  @Autowired
  private PixProviderRepository pixProviderRepository;

  @Autowired
  private PixProviderService pixProviderService;

  @GetMapping()
  public List<PixProvider> findAll() {
    return pixProviderRepository.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<PixProvider> findById(@RequestParam String id) {
    var mayBePixProvider = pixProviderRepository.findById(id);

    if (!mayBePixProvider.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    var pixProvider = mayBePixProvider.get();

    return ResponseEntity.ok(pixProvider);
  }

  @PostMapping()
  public ResponseEntity<PixProvider> save(@RequestBody PixProvider pixProvider) {
    var savedPixProvider = pixProviderRepository.save(pixProvider);

    var uri = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(savedPixProvider.getId())
        .toUri();

    return ResponseEntity.created(uri).body(savedPixProvider);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PixProvider> update(@RequestParam String id, @RequestBody PixProvider pixProvider) {
    var mayBePixProvider = pixProviderRepository.findById(id);

    if (!mayBePixProvider.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    pixProvider.setId(id);

    return ResponseEntity.ok(pixProviderRepository.save(pixProvider));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@RequestParam String id) {
    var mayBePixProvider = pixProviderRepository.findById(id);

    if (!mayBePixProvider.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    pixProviderRepository.deleteById(id);

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/provider-for-sales-volume/{salesVolume}")
  public ResponseEntity<?> findByName(@RequestParam double salesVolume) {
    var pixProviderWithFee = this.pixProviderService.getPixProviderWithFee(salesVolume);

    if (pixProviderWithFee == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(pixProviderWithFee);
  }
}
