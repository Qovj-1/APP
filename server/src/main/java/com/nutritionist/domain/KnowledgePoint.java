package com.nutritionist.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "knowledge_points")
public class KnowledgePoint {

  @Id
  @Column(name = "type_id")
  private Integer typeId;

  @Column(nullable = false, length = 10)
  private String name;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(name = "video")
  private byte[] video;

  @Column(precision = 3, scale = 1)
  private BigDecimal difficult;

  @Column(precision = 3, scale = 1)
  private BigDecimal importance;

  @Column(columnDefinition = "TEXT")
  private String example;

  public KnowledgePoint() {}

  public Integer getTypeId() {
    return typeId;
  }

  public void setTypeId(Integer typeId) {
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public byte[] getVideo() {
    return video;
  }

  public void setVideo(byte[] video) {
    this.video = video;
  }

  public BigDecimal getDifficult() {
    return difficult;
  }

  public void setDifficult(BigDecimal difficult) {
    this.difficult = difficult;
  }

  public BigDecimal getImportance() {
    return importance;
  }

  public void setImportance(BigDecimal importance) {
    this.importance = importance;
  }

  public String getExample() {
    return example;
  }

  public void setExample(String example) {
    this.example = example;
  }
}