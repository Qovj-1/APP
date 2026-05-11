package com.nutritionist.web.dto;

import java.util.Map;

public record ExamSubmitRequest(Map<String, String> answers) {}
