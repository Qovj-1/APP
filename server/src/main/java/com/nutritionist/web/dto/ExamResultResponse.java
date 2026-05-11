package com.nutritionist.web.dto;

import java.util.List;

public record ExamResultResponse(int score, int total, int correctCount, List<QuestionResult> questions) {}
