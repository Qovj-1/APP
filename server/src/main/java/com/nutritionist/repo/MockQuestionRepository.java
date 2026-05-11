package com.nutritionist.repo;

import com.nutritionist.domain.MockQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MockQuestionRepository extends JpaRepository<MockQuestion, String> {
  List<MockQuestion> findByKnowledgePoints(Integer knowledgePoints);
}