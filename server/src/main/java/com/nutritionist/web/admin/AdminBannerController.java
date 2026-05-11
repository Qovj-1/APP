package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Banner;
import com.nutritionist.repo.BannerRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/banners")
public class AdminBannerController {

  @Autowired private BannerRepository bannerRepository;

  @GetMapping
  public ApiResult<List<Banner>> list() {
    return ApiResult.ok(bannerRepository.findAll());
  }

  @GetMapping("/{id}")
  public ApiResult<Banner> one(@PathVariable Long id) {
    return ApiResult.ok(bannerRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<Banner> create(@RequestBody Banner body) {
    body.setId(null);
    return ApiResult.ok(bannerRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<Banner> update(@PathVariable Long id, @RequestBody Banner body) {
    if (!bannerRepository.existsById(id)) throw new BusinessException("不存在");
    body.setId(id);
    return ApiResult.ok(bannerRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Long id) {
    bannerRepository.deleteById(id);
    return ApiResult.ok();
  }
}
