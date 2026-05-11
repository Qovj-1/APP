package com.nutritionist.repo;

import com.nutritionist.domain.ExamPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamPaperRepository extends JpaRepository<ExamPaper, Integer> {}