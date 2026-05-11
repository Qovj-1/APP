package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.ExamPaper;
import com.nutritionist.repo.ExamPaperRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/exam-papers")
public class AdminExamPaperController {

  @Autowired private ExamPaperRepository examPaperRepository;

  @GetMapping
  public ApiResult<List<ExamPaper>> list() {
    return ApiResult.ok(examPaperRepository.findAll());
  }

  @GetMapping("/{id}")
  public ApiResult<ExamPaper> one(@PathVariable Integer id) {
    return ApiResult.ok(examPaperRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<ExamPaper> create(@RequestBody ExamPaper body) {
    return ApiResult.ok(examPaperRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<ExamPaper> update(@PathVariable Integer id, @RequestBody ExamPaper body) {
    if (!examPaperRepository.existsById(id)) throw new BusinessException("不存在");
    body.setTypeId(id);
    return ApiResult.ok(examPaperRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    examPaperRepository.deleteById(id);
    return ApiResult.ok();
  }
}