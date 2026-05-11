package com.nutritionist.repo;

import com.nutritionist.domain.ForumPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForumPostRepository extends JpaRepository<ForumPost, Integer> {
  List<ForumPost> findAllByOrderByCreateTimeDesc();
  Page<ForumPost> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}