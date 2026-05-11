package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Admin;
import com.nutritionist.domain.AppUser;
import com.nutritionist.repo.AdminRepository;
import com.nutritionist.repo.AppUserRepository;
import com.nutritionist.web.dto.AppUserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

  @Autowired private AppUserRepository appUserRepository;
  @Autowired private AdminRepository adminRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  public record AdminUserCreateRequest(
      @NotBlank @Size(max = 10) String account,
      @NotBlank @Size(min = 1, max = 10) String password,
      @NotBlank @Size(max = 5) String name,
      @Size(max = 3) String education,
      @Size(max = 13) String phone,
      @Size(max = 15) String address,
      @Size(max = 20) String email) {}

  public record AdminUserUpdateRequest(
      @Size(max = 5) String name,
      @Size(max = 3) String education,
      @Size(max = 13) String phone,
      @Size(max = 15) String address,
      @Size(max = 20) String email,
      @Size(min = 1, max = 10) String newPassword) {}

  @GetMapping
  public ApiResult<Page<AppUserResponse>> page(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<AppUser> page =
        keyword == null || keyword.isBlank()
            ? appUserRepository.findAll(pageable)
            : appUserRepository.findByAccountContainingIgnoreCaseOrNameContainingIgnoreCase(
                keyword, keyword, pageable);
    return ApiResult.ok(page.map(AdminUserController::toDto));
  }

  @GetMapping("/{account}")
  public ApiResult<AppUserResponse> one(@PathVariable String account) {
    AppUser u =
        appUserRepository.findById(account).orElseThrow(() -> new BusinessException("用户不存在"));
    return ApiResult.ok(toDto(u));
  }

  @PostMapping
  public ApiResult<AppUserResponse> create(@Valid @RequestBody AdminUserCreateRequest req) {
    if (appUserRepository.existsById(req.account())) {
      throw new BusinessException("账号已存在");
    }
    AppUser u = new AppUser();
    u.setAccount(req.account());
    u.setPassword(passwordEncoder.encode(req.password()));
    u.setName(req.name());
    u.setEducation(req.education());
    u.setPhone(req.phone());
    u.setAddress(req.address());
    u.setEmail(req.email());
    return ApiResult.ok(toDto(appUserRepository.save(u)));
  }

  @PutMapping("/{account}")
  public ApiResult<AppUserResponse> update(
      @PathVariable String account, @RequestBody AdminUserUpdateRequest req) {
    AppUser u = appUserRepository.findById(account).orElseThrow(() -> new BusinessException("用户不存在"));
    if (req.name() != null) u.setName(req.name());
    if (req.education() != null) u.setEducation(req.education());
    if (req.phone() != null) u.setPhone(req.phone());
    if (req.address() != null) u.setAddress(req.address());
    if (req.email() != null) u.setEmail(req.email());
    if (req.newPassword() != null && !req.newPassword().isBlank()) {
      u.setPassword(passwordEncoder.encode(req.newPassword()));
    }
    return ApiResult.ok(toDto(appUserRepository.save(u)));
  }

  @DeleteMapping("/{account}")
  public ApiResult<Void> delete(@PathVariable String account) {
    appUserRepository.deleteById(account);
    return ApiResult.ok();
  }

  @PostMapping("/{account}/promote")
  public ApiResult<Void> promoteToAdmin(@PathVariable String account) {
    AppUser u = appUserRepository.findById(account)
        .orElseThrow(() -> new BusinessException("用户不存在"));
    
    if (adminRepository.existsById(account)) {
      throw new BusinessException("该用户已是管理员");
    }
    
    Admin admin = new Admin();
    admin.setAccount(u.getAccount());
    admin.setPassword(u.getPassword());
    admin.setName(u.getName());
    adminRepository.save(admin);
    
    return ApiResult.ok();
  }

  private static AppUserResponse toDto(AppUser u) {
    return new AppUserResponse(
        u.getAccount(),
        u.getName(),
        u.getEducation(),
        u.getPhone(),
        u.getAddress(),
        u.getEmail(),
        null);
  }
}