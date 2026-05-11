package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.AppUser;
import com.nutritionist.repo.AppUserRepository;
import com.nutritionist.support.CurrentUser;
import com.nutritionist.web.dto.AppUserResponse;
import com.nutritionist.web.dto.ChangePasswordRequest;
import com.nutritionist.web.dto.UpdateProfileRequest;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/app/me")
public class AppProfileController {

  @Autowired private AppUserRepository appUserRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  private static final String UPLOAD_DIR = "uploads/avatars";

  @GetMapping
  public ApiResult<AppUserResponse> me() {
    var opt = appUserRepository.findById(CurrentUser.account());
    if (opt.isPresent()) {
      return ApiResult.ok(toDto(opt.get()));
    }
    AppUser demo = new AppUser();
    demo.setAccount("demo");
    demo.setName("演示学员");
    demo.setEducation("本科");
    demo.setPhone("13800000000");
    demo.setEmail("demo@example.com");
    return ApiResult.ok(toDto(demo));
  }

  @PutMapping
  public ApiResult<AppUserResponse> update(@Valid @RequestBody UpdateProfileRequest req) {
    AppUser u = appUserRepository.findById(CurrentUser.account()).orElse(null);
    if (u == null) {
      u = new AppUser();
      u.setAccount(CurrentUser.account());
      u.setPassword(passwordEncoder.encode("123456"));
    }
    if (req.name() != null) u.setName(req.name());
    if (req.education() != null) u.setEducation(req.education());
    if (req.phone() != null) u.setPhone(req.phone());
    if (req.address() != null) u.setAddress(req.address());
    if (req.email() != null) u.setEmail(req.email());
    return ApiResult.ok(toDto(appUserRepository.save(u)));
  }

  @PostMapping("/avatar")
  public ApiResult<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
    try {
      String originalFilename = file.getOriginalFilename();
      String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
      String newFilename = UUID.randomUUID().toString() + extension;

      File uploadDir = new File(UPLOAD_DIR);
      if (!uploadDir.exists()) {
        uploadDir.mkdirs();
      }

      Path filePath = Paths.get(UPLOAD_DIR, newFilename);
      Files.write(filePath, file.getBytes());

      AppUser u = appUserRepository.findById(CurrentUser.account()).orElse(null);
      if (u == null) {
        u = new AppUser();
        u.setAccount(CurrentUser.account());
        u.setPassword(passwordEncoder.encode("123456"));
      }

      String avatarUrl = "/api/uploads/avatars/" + newFilename;
      u.setAvatarUrl(avatarUrl);
      appUserRepository.save(u);

      Map<String, String> result = new HashMap<>();
      result.put("url", avatarUrl);
      return ApiResult.ok(result);
    } catch (IOException e) {
      throw new BusinessException("头像上传失败");
    }
  }

  @PostMapping("/password")
  public ApiResult<Void> password(@Valid @RequestBody ChangePasswordRequest req) {
    AppUser u =
        appUserRepository
            .findById(CurrentUser.account())
            .orElseThrow(() -> new BusinessException("用户不存在"));
    if (!passwordEncoder.matches(req.oldPassword(), u.getPassword())) {
      throw new BusinessException("原密码错误");
    }
    u.setPassword(passwordEncoder.encode(req.newPassword()));
    appUserRepository.save(u);
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
        u.getAvatarUrl());
  }
}