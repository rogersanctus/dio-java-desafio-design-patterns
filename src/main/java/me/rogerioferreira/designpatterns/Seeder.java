package me.rogerioferreira.designpatterns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.repositories.PixProviderRepository;

@Component
public class Seeder implements CommandLineRunner {
  @Autowired
  private PixProviderRepository pixProviderRepository;

  @Override
  public void run(String... args) throws Exception {
    pixProviderRepository.save(new PixProvider("123-4", "DasQuantas", 0.8, 1.9, 100000, 500000));
    pixProviderRepository.save(new PixProvider("321-5", "PagueMais", 0.45, 2.7, 250000, 1000000));

    System.out.println("Seeders executados");
  }

}
