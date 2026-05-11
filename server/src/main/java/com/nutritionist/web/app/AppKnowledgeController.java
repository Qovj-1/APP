package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.KnowledgePoint;
import com.nutritionist.repo.KnowledgePointRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/knowledge")
public class AppKnowledgeController {

  @Autowired private KnowledgePointRepository knowledgePointRepository;

  @GetMapping
  public ApiResult<List<KnowledgePoint>> list() {
    return ApiResult.ok(knowledgePointRepository.findAll());
  }

  @GetMapping("/{id}")
  public ApiResult<KnowledgePoint> detail(@PathVariable Integer id) {
    KnowledgePoint k =
        knowledgePointRepository.findById(id).orElseThrow(() -> new BusinessException("知识点不存在"));
    return ApiResult.ok(k);
  }
}