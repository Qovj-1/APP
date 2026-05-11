package com.nutritionist.repo;

import com.nutritionist.domain.KnowledgePoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgePointRepository extends JpaRepository<KnowledgePoint, Integer> {}