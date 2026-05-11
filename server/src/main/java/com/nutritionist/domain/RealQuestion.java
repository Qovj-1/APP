package com.nutritionist.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "real_questions")
public class RealQuestion {

  @Id
  @Column(name = "type_id", length = 10)
  private String typeId;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String title;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String answer;

  @Column(name = "year")
  private Instant year;

  @Column(name = "knowledge_points")
  private Integer knowledgePoints;

  @Column(precision = 3, scale = 1)
  private BigDecimal difficult;

  @Column(name = "analysis", columnDefinition = "TEXT")
  private String analysis;

  public RealQuestion() {}

  public String getTypeId() {
    return typeId;
  }

  public void setTypeId(String typeId) {
    this.typeId = typeId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }

  public Instant getYear() {
    return year;
  }

  public void setYear(Instant year) {
    this.year = year;
  }

  public Integer getKnowledgePoints() {
    return knowledgePoints;
  }

  public void setKnowledgePoints(Integer knowledgePoints) {
    this.knowledgePoints = knowledgePoints;
  }

  public BigDecimal getDifficult() {
    return difficult;
  }

  public void setDifficult(BigDecimal difficult) {
    this.difficult = difficult;
  }

  public String getAnalysis() {
    return analysis;
  }

  public void setAnalysis(String analysis) {
    this.analysis = analysis;
  }

  public String getQuestionType() {
    return "CASE";
  }
}