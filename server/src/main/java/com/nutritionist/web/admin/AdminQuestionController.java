package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Exercise;
import com.nutritionist.domain.MockQuestion;
import com.nutritionist.domain.RealQuestion;
import com.nutritionist.repo.ExerciseRepository;
import com.nutritionist.repo.MockQuestionRepository;
import com.nutritionist.repo.RealQuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/questions")
public class AdminQuestionController {

  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private MockQuestionRepository mockQuestionRepository;
  @Autowired private RealQuestionRepository realQuestionRepository;

  @GetMapping
  public ApiResult<List<Map<String, Object>>> list(
      @RequestParam(required = false) String type,
      @RequestParam(required = false) String keyword) {
    List<Map<String, Object>> result = new ArrayList<>();
    
    if (type == null || type.isBlank() || "exercise".equals(type)) {
      exerciseRepository.findAll().forEach(e -> {
        if (keyword == null || keyword.isBlank() || e.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
          result.add(Map.of(
            "id", e.getId(),
            "title", e.getTitle(),
            "answer", e.getAnswer(),
            "difficult", e.getDifficult(),
            "qType", "exercise"
          ));
        }
      });
    }
    
    if (type == null || type.isBlank() || "multi".equals(type)) {
      mockQuestionRepository.findAll().forEach(m -> {
        String answer = m.getAnswer();
        if (answer != null && answer.contains(",") && 
            (keyword == null || keyword.isBlank() || m.getTitle().toLowerCase().contains(keyword.toLowerCase()))) {
          result.add(Map.of(
            "id", m.getTypeId(),
            "title", m.getTitle(),
            "answer", m.getAnswer(),
            "difficult", m.getDifficult(),
            "qType", "multi"
          ));
        }
      });
    }
    
    if (type == null || type.isBlank() || "case".equals(type)) {
      realQuestionRepository.findAll().forEach(r -> {
        if (keyword == null || keyword.isBlank() || r.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
          result.add(Map.of(
            "id", r.getTypeId(),
            "title", r.getTitle(),
            "answer", r.getAnswer(),
            "difficult", r.getDifficult(),
            "qType", "case"
          ));
        }
      });
    }
    
    return ApiResult.ok(result);
  }

  @GetMapping("/{id}")
  public ApiResult<Exercise> one(@PathVariable Integer id) {
    return ApiResult.ok(exerciseRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<Exercise> create(@RequestBody Exercise body) {
    body.setId(null);
    return ApiResult.ok(exerciseRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<Exercise> update(@PathVariable Integer id, @RequestBody Exercise body) {
    if (!exerciseRepository.existsById(id)) throw new BusinessException("不存在");
    body.setId(id);
    return ApiResult.ok(exerciseRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    exerciseRepository.deleteById(id);
    return ApiResult.ok();
  }
}