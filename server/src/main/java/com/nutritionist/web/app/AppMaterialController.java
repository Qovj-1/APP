package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.domain.Material;
import com.nutritionist.repo.MaterialRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/materials")
public class AppMaterialController {

  @Autowired private MaterialRepository materialRepository;

  @GetMapping
  public ApiResult<List<Material>> list() {
    return ApiResult.ok(materialRepository.findAll());
  }

  @GetMapping("/by-course/{courseId}")
  public ApiResult<List<Material>> byCourse(@PathVariable Integer courseId) {
    return ApiResult.ok(materialRepository.findAll());
  }
}