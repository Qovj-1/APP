package com.nutritionist.repo;

import com.nutritionist.domain.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, String> {
    List<Training> findByNameContainingIgnoreCase(String name);
    Page<Training> findByNameContainingIgnoreCase(String name, Pageable pageable);
}