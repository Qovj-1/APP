package com.nutritionist.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.BusinessException;
import com.nutritionist.config.JwtService;
import com.nutritionist.domain.AppUser;
import com.nutritionist.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppAuthService {

  @Autowired private AppUserRepository appUserRepository;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtService jwtService;

  @Transactional
  public void register(String account, String password, String name, String phone) {
    if (appUserRepository.existsById(account)) {
      throw new BusinessException("账号已存在");
    }
    AppUser u = new AppUser();
    u.setAccount(account);
    u.setPassword(passwordEncoder.encode(password));
    u.setName(name);
    u.setPhone(phone);
    appUserRepository.save(u);
  }

  public String login(String account, String password) {
    AppUser u =
        appUserRepository
            .findById(account)
            .orElseThrow(() -> new BusinessException(401, "账号或密码错误"));
    if (!passwordEncoder.matches(password, u.getPassword())) {
      throw new BusinessException(401, "账号或密码错误");
    }
    return jwtService.createToken(u.getAccount(), "USER");
  }
}