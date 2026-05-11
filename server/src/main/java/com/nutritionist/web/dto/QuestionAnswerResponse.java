package com.nutritionist.web.dto;

public record QuestionAnswerResponse(
    boolean correct, String standardAnswer, String analysis) {}
