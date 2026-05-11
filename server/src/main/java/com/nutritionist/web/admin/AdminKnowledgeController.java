package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.KnowledgePoint;
import com.nutritionist.repo.KnowledgePointRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/knowledge")
public class AdminKnowledgeController {

  @Autowired private KnowledgePointRepository knowledgePointRepository;

  @GetMapping
  public ApiResult<List<KnowledgePoint>> list() {
    return ApiResult.ok(knowledgePointRepository.findAll());
  }

  @GetMapping("/{id}")
  public ApiResult<KnowledgePoint> one(@PathVariable Integer id) {
    return ApiResult.ok(
        knowledgePointRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<KnowledgePoint> create(@RequestBody KnowledgePoint body) {
    return ApiResult.ok(knowledgePointRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<KnowledgePoint> update(@PathVariable Integer id, @RequestBody KnowledgePoint body) {
    if (!knowledgePointRepository.existsById(id)) throw new BusinessException("不存在");
    body.setTypeId(id);
    return ApiResult.ok(knowledgePointRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    knowledgePointRepository.deleteById(id);
    return ApiResult.ok();
  }
}