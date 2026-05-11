package com.nutritionist.web.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @Size(max = 64) String name,
    @Size(max = 64) String education,
    @Size(max = 32) String phone,
    @Size(max = 255) String address,
    @Size(max = 255) String email,
    @Size(max = 512) String avatarUrl) {}
