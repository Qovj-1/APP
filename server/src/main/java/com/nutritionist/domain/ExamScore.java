package com.nutritionist.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "scores")
public class ExamScore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 5)
  private String name;

  @Column(name = "paper_id")
  private Integer paperId;

  @Column(name = "exam_time")
  private Instant examTime;

  private Integer score;

  public ExamScore() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPaperId() {
    return paperId;
  }

  public void setPaperId(Integer paperId) {
    this.paperId = paperId;
  }

  public Instant getExamTime() {
    return examTime;
  }

  public void setExamTime(Instant examTime) {
    this.examTime = examTime;
  }

  public Integer getScore() {
    return score;
  }

  public void setScore(Integer score) {
    this.score = score;
  }
}