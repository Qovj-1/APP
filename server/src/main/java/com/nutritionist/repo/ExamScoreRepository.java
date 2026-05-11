package com.nutritionist.repo;

import com.nutritionist.domain.ExamScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamScoreRepository extends JpaRepository<ExamScore, Integer> {
  List<ExamScore> findByNameOrderByExamTimeDesc(String name);
}