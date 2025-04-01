package me.rogerioferreira.designpatterns;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import me.rogerioferreira.designpatterns.models.PixProvider;
import me.rogerioferreira.designpatterns.models.User;
import me.rogerioferreira.designpatterns.repositories.PixProviderRepository;
import me.rogerioferreira.designpatterns.repositories.UserRepository;

@Component
public class Seeder implements CommandLineRunner {
  @Autowired
  private PixProviderRepository pixProviderRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public void run(String... args) throws Exception {
    userRepository.save(new User("555-6", "Jeph Meh", "Jeph Company", 125000.98));
    userRepository.save(new User("654-7", "Esperanto Na Cuia", "Esperanto Company", 250000.00));

    pixProviderRepository.save(new PixProvider("123-4", "DasQuantas", 0.8, 1.9, 100000, 500000));
    pixProviderRepository.save(new PixProvider("321-5", "PagueMais", 0.45, 2.7, 250000, 1000000));

    System.out.println("Seeders executados");
  }

}
