package com.nutritionist.repo;

import com.nutritionist.domain.RealQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RealQuestionRepository extends JpaRepository<RealQuestion, String> {
  List<RealQuestion> findByKnowledgePoints(Integer knowledgePoints);
}