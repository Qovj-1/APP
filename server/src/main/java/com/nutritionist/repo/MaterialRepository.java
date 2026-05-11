package com.nutritionist.repo;

import com.nutritionist.domain.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
  Page<Material> findByNameContainingIgnoreCase(String name, Pageable pageable);
}