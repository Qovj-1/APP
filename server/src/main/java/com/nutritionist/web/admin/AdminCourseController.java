package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Course;
import com.nutritionist.repo.CourseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
public class AdminCourseController {

  @Autowired private CourseRepository courseRepository;

  @GetMapping
  public ApiResult<Page<Course>> list(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<Course> page =
        keyword == null || keyword.isBlank()
            ? courseRepository.findAll(pageable)
            : courseRepository.findByNameContainingIgnoreCase(keyword, pageable);
    return ApiResult.ok(page);
  }

  @GetMapping("/{id}")
  public ApiResult<Course> one(@PathVariable Integer id) {
    return ApiResult.ok(courseRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping
  public ApiResult<Course> create(@RequestBody Course body) {
    body.setId(null);
    return ApiResult.ok(courseRepository.save(body));
  }

  @PutMapping("/{id}")
  public ApiResult<Course> update(@PathVariable Integer id, @RequestBody Course body) {
    if (!courseRepository.existsById(id)) throw new BusinessException("不存在");
    body.setId(id);
    return ApiResult.ok(courseRepository.save(body));
  }

  @DeleteMapping("/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    courseRepository.deleteById(id);
    return ApiResult.ok();
  }
}