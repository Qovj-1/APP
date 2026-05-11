package com.nutritionist.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.BusinessException;
import com.nutritionist.config.JwtService;
import com.nutritionist.domain.Admin;
import com.nutritionist.repo.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminAuthService {

  @Autowired private AdminRepository adminRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtService jwtService;

  public String login(String account, String password) {
    Admin a =
        adminRepository
            .findById(account)
            .orElseThrow(() -> new BusinessException(401, "账号或密码错误"));
    if (!passwordEncoder.matches(password, a.getPassword())) {
      throw new BusinessException(401, "账号或密码错误");
    }
    return jwtService.createToken(a.getAccount(), "ADMIN");
  }
}