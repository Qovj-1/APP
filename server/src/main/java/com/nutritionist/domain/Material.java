package com.nutritionist.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "materials")
public class Material {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

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

  public Material() {}

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
}