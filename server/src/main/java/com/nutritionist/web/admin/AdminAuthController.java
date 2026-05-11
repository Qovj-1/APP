package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.domain.Admin;
import com.nutritionist.repo.AdminRepository;
import com.nutritionist.service.AdminAuthService;
import com.nutritionist.web.dto.LoginRequest;
import com.nutritionist.web.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

  @Autowired private AdminAuthService adminAuthService;
  @Autowired private AdminRepository adminRepository;

  @PostMapping("/login")
  public ApiResult<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
    String token = adminAuthService.login(req.account(), req.password());
    Admin a =
        adminRepository
            .findById(req.account())
            .orElseThrow(() -> new IllegalStateException("admin missing"));
    return ApiResult.ok(new TokenResponse(token, a.getAccount(), a.getName()));
  }
}
