package com.nutritionist.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "mock_questions")
public class MockQuestion {

  @Id
  @Column(name = "type_id", length = 10)
  private String typeId;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String title;

  @Column(nullable = false, columnDefinition = "TINYTEXT")
  private String answer;

  @Column(name = "option_a", length = 100)
  private String optionA;

  @Column(name = "option_b", length = 100)
  private String optionB;

  @Column(name = "option_c", length = 100)
  private String optionC;

  @Column(name = "option_d", length = 100)
  private String optionD;

  @Column(name = "knowledge_points")
  private Integer knowledgePoints;

  @Column(precision = 3, scale = 1)
  private BigDecimal difficult;

  @Column(name = "analysis", columnDefinition = "TEXT")
  private String analysis;

  public MockQuestion() {}

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

  public String getOptionA() {
    return optionA;
  }

  public void setOptionA(String optionA) {
    this.optionA = optionA;
  }

  public String getOptionB() {
    return optionB;
  }

  public void setOptionB(String optionB) {
    this.optionB = optionB;
  }

  public String getOptionC() {
    return optionC;
  }

  public void setOptionC(String optionC) {
    this.optionC = optionC;
  }

  public String getOptionD() {
    return optionD;
  }

  public void setOptionD(String optionD) {
    this.optionD = optionD;
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
    return "MULTI";
  }
}