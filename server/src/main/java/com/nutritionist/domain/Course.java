package com.nutritionist.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, length = 10)
  private String name;

  @Column(name = "teacher_profile", columnDefinition = "TINYTEXT")
  private String teacherProfile;

  @Column(columnDefinition = "TINYTEXT")
  private String introduction;

  @Column(name = "video_url", length = 500)
  private String videoUrl;

  @Column(name = "video_title", length = 100)
  private String videoTitle;

  @Column(name = "video_poster", length = 500)
  private String videoPoster;

  @Column(name = "video")
  private byte[] video;

  @Column(name = "time")
  private Short time;

  @Column(length = 5)
  private String star;

  public Course() {}

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

  public String getTeacherProfile() {
    return teacherProfile;
  }

  public void setTeacherProfile(String teacherProfile) {
    this.teacherProfile = teacherProfile;
  }

  public String getIntroduction() {
    return introduction;
  }

  public void setIntroduction(String introduction) {
    this.introduction = introduction;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getVideoTitle() {
    return videoTitle;
  }

  public void setVideoTitle(String videoTitle) {
    this.videoTitle = videoTitle;
  }

  public String getVideoPoster() {
    return videoPoster;
  }

  public void setVideoPoster(String videoPoster) {
    this.videoPoster = videoPoster;
  }

  public byte[] getVideo() {
    return video;
  }

  public void setVideo(byte[] video) {
    this.video = video;
  }

  public Short getTime() {
    return time;
  }

  public void setTime(Short time) {
    this.time = time;
  }

  public String getStar() {
    return star;
  }

  public void setStar(String star) {
    this.star = star;
  }
}