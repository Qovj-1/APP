package com.nutritionist.repo;

import com.nutritionist.domain.Banner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepository extends JpaRepository<Banner, Long> {
  List<Banner> findByEnabledTrueOrderBySortOrderAsc();
}
