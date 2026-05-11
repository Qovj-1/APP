package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Material;
import com.nutritionist.repo.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/materials")
public class AdminMaterialController {

  @Autowired private MaterialRepository materialRepository;

  @GetMapping
  public ApiResult<Page<Material>> list(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<Material> page =
        keyword == null || keyword.isBlank()
            ? materialRepository.findAll(pageable)
            : materialRepository.findByNameContainingIgnoreCase(keyword, pageable);
    return ApiResult.ok(page);
  }

  @GetMapping("/{id}")
  public ApiResult<Material> one(@PathVariable Integer id) {
    return ApiResult.ok(materialRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<Material> create(@RequestBody Material body) {
    body.setId(null);
    return ApiResult.ok(materialRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<Material> update(@PathVariable Integer id, @RequestBody Material body) {
    if (!materialRepository.existsById(id)) throw new BusinessException("不存在");
    body.setId(id);
    return ApiResult.ok(materialRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    materialRepository.deleteById(id);
    return ApiResult.ok();
  }
}