package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Exercise;
import com.nutritionist.domain.MockQuestion;
import com.nutritionist.domain.RealQuestion;
import com.nutritionist.repo.ExerciseRepository;
import com.nutritionist.repo.MockQuestionRepository;
import com.nutritionist.repo.RealQuestionRepository;
import com.nutritionist.web.dto.QuestionAnswerRequest;
import com.nutritionist.web.dto.QuestionAnswerResponse;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/questions")
public class AppQuestionController {

  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private MockQuestionRepository mockQuestionRepository;
  @Autowired private RealQuestionRepository realQuestionRepository;

  @GetMapping
  public ApiResult<List<Object>> list(@RequestParam String source, @RequestParam(required = false) String type) {
    List<Object> list = new ArrayList<>();
    if ("EXERCISE".equals(source)) {
      list.addAll(exerciseRepository.findAll());
    } else if ("MOCK".equals(source)) {
      list.addAll(mockQuestionRepository.findAll());
    } else if ("REAL".equals(source)) {
      list.addAll(realQuestionRepository.findAll());
    }
    return ApiResult.ok(list);
  }

  @GetMapping("/{id}")
  public ApiResult<?> detail(@PathVariable String id) {
    try {
      Integer intId = Integer.parseInt(id);
      return ApiResult.ok(exerciseRepository.findById(intId).orElseThrow(() -> new BusinessException("题目不存在")));
    } catch (NumberFormatException e) {
      var mock = mockQuestionRepository.findById(id);
      if (mock.isPresent()) {
        return ApiResult.ok(mock.get());
      }
      return ApiResult.ok(realQuestionRepository.findById(id).orElseThrow(() -> new BusinessException("题目不存在")));
    }
  }

  @PostMapping("/{id}/answer")
  public ApiResult<QuestionAnswerResponse> answer(
      @PathVariable String id, @Valid @RequestBody QuestionAnswerRequest body) {
    try {
      Integer intId = Integer.parseInt(id);
      return exerciseRepository.findById(intId)
          .map(q -> {
            boolean ok = q.getAnswer().equalsIgnoreCase(body.userAnswer().trim()) 
                || q.getAnswer().contains(body.userAnswer().trim());
            return ApiResult.ok(new QuestionAnswerResponse(ok, q.getAnswer(), ""));
          })
          .orElseThrow(() -> new BusinessException("题目不存在"));
    } catch (NumberFormatException e) {
      var mock = mockQuestionRepository.findById(id);
      if (mock.isPresent()) {
        var q = mock.get();
        boolean ok = q.getAnswer().equalsIgnoreCase(body.userAnswer().trim()) 
            || q.getAnswer().contains(body.userAnswer().trim());
        return ApiResult.ok(new QuestionAnswerResponse(ok, q.getAnswer(), ""));
      }
      var real = realQuestionRepository.findById(id);
      if (real.isPresent()) {
        var q = real.get();
        boolean ok = q.getAnswer().equalsIgnoreCase(body.userAnswer().trim()) 
            || q.getAnswer().contains(body.userAnswer().trim());
        return ApiResult.ok(new QuestionAnswerResponse(ok, q.getAnswer(), ""));
      }
      throw new BusinessException("题目不存在");
    }
  }
}