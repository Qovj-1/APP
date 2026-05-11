package com.nutritionist.web.dto;

public record AppUserResponse(
    String account,
    String name,
    String education,
    String phone,
    String address,
    String email,
    String avatarUrl) {}
