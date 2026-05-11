package com.nutritionist.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ForumPostRequest(
    @NotBlank @Size(max = 255) String title, 
    @NotBlank String content,
    String authorName) {}
