package me.rogerioferreira.designpatterns.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PixProviderRepository;

@RestController
@RequestMapping("/pix-providers")
public class PixProviderController {
  @Autowired
  private PixProviderRepository pixProviderRepository;

  @GetMapping()
  public List<PixProvider> findAll() {
    return pixProviderRepository.findAll();
  }
}
