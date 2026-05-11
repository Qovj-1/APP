package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Course;
import com.nutritionist.repo.CourseRepository;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/courses")
public class AppCourseController {

  @Autowired private CourseRepository courseRepository;

  @GetMapping
  public ApiResult<List<Course>> list() {
    return ApiResult.ok(courseRepository.findAll());
  }

  @GetMapping("/{id}")
  public ApiResult<Course> detail(@PathVariable Integer id) {
    Course c = courseRepository.findById(id).orElseThrow(() -> new BusinessException("课程不存在"));
    return ApiResult.ok(c);
  }
}