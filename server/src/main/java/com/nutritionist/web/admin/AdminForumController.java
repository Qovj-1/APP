package com.nutritionist.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.ForumPost;
import com.nutritionist.repo.ForumPostRepository;
import com.nutritionist.web.dto.ForumPostRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/forum")
public class AdminForumController {

  @Autowired private ForumPostRepository forumPostRepository;

  @GetMapping
  public ApiResult<Page<ForumPost>> list(
      @RequestParam(required = false) String keyword,
      @PageableDefault(size = 20) Pageable pageable) {
    Page<ForumPost> page =
        keyword == null || keyword.isBlank()
            ? forumPostRepository.findAll(pageable)
            : forumPostRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    return ApiResult.ok(page);
  }

  @GetMapping("/posts")
  public ApiResult<List<ForumPost>> listAll() {
    return ApiResult.ok(forumPostRepository.findAllByOrderByCreateTimeDesc());
  }

  @GetMapping("/posts/{id}")
  public ApiResult<ForumPost> one(@PathVariable Integer id) {
    return ApiResult.ok(forumPostRepository.findById(id).orElseThrow(() -> new BusinessException("不存在")));
  }

  @PostMapping("/posts")
  public ApiResult<ForumPost> create(@Valid @RequestBody ForumPostRequest req, @RequestParam String userAccount) {
    ForumPost p = new ForumPost();
    p.setTitle(req.title());
    p.setContent(req.content());
    p.setUserAccount(userAccount);
    p.setCreateTime(Instant.now());
    return ApiResult.ok(forumPostRepository.save(p));
  }

  @PutMapping("/posts/{id}")
  public ApiResult<ForumPost> update(@PathVariable Integer id, @Valid @RequestBody ForumPostRequest req) {
    ForumPost p = forumPostRepository.findById(id).orElseThrow(() -> new BusinessException("不存在"));
    p.setTitle(req.title());
    p.setContent(req.content());
    if (req.authorName() != null && !req.authorName().isBlank()) {
      p.setUserAccount(req.authorName());
    }
    return ApiResult.ok(forumPostRepository.save(p));
  }

  @DeleteMapping("/posts/{id}")
  public ApiResult<Void> delete(@PathVariable Integer id) {
    forumPostRepository.deleteById(id);
    return ApiResult.ok();
  }
}