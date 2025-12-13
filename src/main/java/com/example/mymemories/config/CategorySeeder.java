package com.example.mymemories.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.mymemories.entity.Category;
import com.example.mymemories.repository.CategoryRepository;

@Component
public class CategorySeeder implements CommandLineRunner {
  private final CategoryRepository categoryRepository;
  public CategorySeeder(CategoryRepository categoryRepository) {
      this.categoryRepository = categoryRepository;
  }
  @Override
  public void run(String... args) throws Exception {
      List<String> bongchangksr = Arrays.asList("Tour", "Party", "Love", "Family", "Friends", "Study", "Personal");
   System.out.println(bongchangksr);
   
      for (String name : bongchangksr) {
          if (!categoryRepository.existsByName(name)) {
              categoryRepository.save(new Category());
          }
      }
  }
  
}
