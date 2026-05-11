package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.domain.Banner;
import com.nutritionist.repo.BannerRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/banners")
public class AppBannerController {

  @Autowired private BannerRepository bannerRepository;

  @GetMapping
  public ApiResult<List<Banner>> list() {
    return ApiResult.ok(bannerRepository.findByEnabledTrueOrderBySortOrderAsc());
  }
}
