package com.nutritionist.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "banners")
public class Banner {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "image_url", nullable = false, length = 512)
  private String imageUrl;

  @Column(name = "link_url", length = 512)
  private String linkUrl;

  @Column(name = "sort_order", nullable = false)
  private Integer sortOrder = 0;

  @Column(nullable = false)
  private Boolean enabled = true;

  public Banner() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public void setLinkUrl(String linkUrl) {
    this.linkUrl = linkUrl;
  }

  public Integer getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(Integer sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }
}
