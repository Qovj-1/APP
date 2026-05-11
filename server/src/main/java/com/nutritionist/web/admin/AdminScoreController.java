package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.ExamScore;
import com.nutritionist.repo.ExamScoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/scores")
public class AdminScoreController {

  @Autowired private ExamScoreRepository examScoreRepository;

  @GetMapping
  public ApiResult<Page<ExamScore>> page(@PageableDefault(size = 20) Pageable pageable) {
    return ApiResult.ok(examScoreRepository.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ApiResult<ExamScore> one(@PathVariable Integer id) {
    return ApiResult.ok(examScoreRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    examScoreRepository.deleteById(id);
    return ApiResult.ok();
  }
}