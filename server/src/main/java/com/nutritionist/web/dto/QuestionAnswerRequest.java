package com.nutritionist.web.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionAnswerRequest(@NotBlank String userAnswer) {}
