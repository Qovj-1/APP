package com.nutritionist.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "exam_papers")
public class ExamPaper {

  @Id
  @Column(name = "type_id")
  private Integer typeId;

  @Column(name = "exam_time")
  private Instant examTime;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String problem;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String answer;

  @Column(precision = 3, scale = 1)
  private BigDecimal difficult;

  public ExamPaper() {}

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public Instant getExamTime() {
    return examTime;
  }

  public void setExamTime(Instant examTime) {
    this.examTime = examTime;
  }

  public String getProblem() {
    return problem;
  }

  public void setProblem(String problem) {
    this.problem = problem;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public BigDecimal getDifficult() {
    return difficult;
  }

  public void setDifficult(BigDecimal difficult) {
    this.difficult = difficult;
  }
}