package com.nutritionist.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank @Size(max = 64) String account,
    @NotBlank @Size(min = 6, max = 64) String password,
    @NotBlank @Size(max = 64) String name,
    @Size(max = 32) String phone) {}
