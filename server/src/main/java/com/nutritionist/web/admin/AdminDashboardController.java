package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.repo.*;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

  @Autowired private AppUserRepository appUserRepository;
  @Autowired private CourseRepository courseRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private ExamScoreRepository examScoreRepository;
  @Autowired private ForumPostRepository forumPostRepository;

  @GetMapping("/stats")
  public ApiResult<Map<String, Object>> stats() {
    Map<String, Object> m = new LinkedHashMap<>();
    m.put("users", appUserRepository.count());
    m.put("courses", courseRepository.count());
    m.put("questions", exerciseRepository.count());
    m.put("scores", examScoreRepository.count());
    m.put("posts", forumPostRepository.count());
    return ApiResult.ok(m);
  }
}