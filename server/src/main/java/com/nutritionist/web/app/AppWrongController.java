package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.repo.ExerciseRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/wrongs")
public class AppWrongController {

  @Autowired private ExerciseRepository exerciseRepository;

  @GetMapping
  public ApiResult<List<Object>> list() {
    return ApiResult.ok(exerciseRepository.findAll().stream().map(e -> (Object) e).toList());
  }

  @DeleteMapping("/{questionId}")
  public ApiResult<Void> remove(@PathVariable Integer questionId) {
    exerciseRepository.deleteById(questionId);
    return ApiResult.ok();
  }
}