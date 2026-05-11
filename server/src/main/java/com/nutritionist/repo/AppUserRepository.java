package com.nutritionist.repo;

import com.nutritionist.domain.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

  Page<AppUser> findByAccountContainingIgnoreCaseOrNameContainingIgnoreCase(
      String a, String n, Pageable pageable);
}
