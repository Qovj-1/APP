package com.nutritionist.support;

import org.springframework.security.core.context.SecurityContextHolder;

public final class CurrentUser {

  private CurrentUser() {}

  public static String account() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}
