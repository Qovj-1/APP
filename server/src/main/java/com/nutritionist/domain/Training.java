package com.nutritionist.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @Column(name = "_training_id", length = 10)
    private String trainingId;

    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String name;

    @Column(columnDefinition = "TINYTEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] video;

    @Column(nullable = false)
    private Short time;

    @Column(name = "knowledge_points", length = 10, nullable = false)
    private String knowledgePoints;

    @Column(name = "difficult", nullable = false)
    private Double difficult;

    public String getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(String trainingId) {
        this.trainingId = trainingId;
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

    public Short getTime() {
        return time;
    }

    public void setTime(Short time) {
        this.time = time;
    }

    public String getKnowledgePoints() {
        return knowledgePoints;
    }

    public void setKnowledgePoints(String knowledgePoints) {
        this.knowledgePoints = knowledgePoints;
    }

    public Double getDifficult() {
        return difficult;
    }

    public void setDifficult(Double difficult) {
        this.difficult = difficult;
    }
}