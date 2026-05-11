package com.nutritionist.web.dto;

public record QuestionResult(
    String id,
    String type,
    String title,
    String userAnswer,
    String correctAnswer,
    Boolean isCorrect,
    String analysis
) {}
