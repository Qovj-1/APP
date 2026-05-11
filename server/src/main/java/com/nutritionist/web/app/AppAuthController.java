package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.domain.AppUser;
import com.nutritionist.repo.AppUserRepository;
import com.nutritionist.service.AppAuthService;
import com.nutritionist.web.dto.LoginRequest;
import com.nutritionist.web.dto.RegisterRequest;
import com.nutritionist.web.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/auth")
public class AppAuthController {

  @Autowired private AppAuthService appAuthService;
  @Autowired private AppUserRepository appUserRepository;

  @PostMapping("/register")
  public ApiResult<Void> register(@Valid @RequestBody RegisterRequest req) {
    appAuthService.register(req.account(), req.password(), req.name(), req.phone());
    return ApiResult.ok();
  }

  @PostMapping("/login")
  public ApiResult<TokenResponse> login(@Valid @RequestBody LoginRequest req) {
    String token = appAuthService.login(req.account(), req.password());
    AppUser u =
        appUserRepository
            .findById(req.account())
            .orElseThrow(() -> new IllegalStateException("user missing"));
    return ApiResult.ok(new TokenResponse(token, u.getAccount(), u.getName()));
  }
}
