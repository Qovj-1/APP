package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.ForumPost;
import com.nutritionist.repo.ForumPostRepository;
import com.nutritionist.support.CurrentUser;
import com.nutritionist.web.dto.ForumPostRequest;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/forum")
public class AppForumController {

  @Autowired private ForumPostRepository forumPostRepository;

  @GetMapping("/posts")
  public ApiResult<List<ForumPost>> list() {
    return ApiResult.ok(forumPostRepository.findAllByOrderByCreateTimeDesc());
  }

  @GetMapping("/posts/{id}")
  public ApiResult<ForumPost> detail(@PathVariable Integer id) {
    ForumPost p = forumPostRepository.findById(id).orElseThrow(() -> new BusinessException("帖子不存在"));
    return ApiResult.ok(p);
  }

  @PostMapping("/posts")
  public ApiResult<ForumPost> create(@Valid @RequestBody ForumPostRequest req) {
    ForumPost p = new ForumPost();
    p.setTitle(req.title());
    p.setContent(req.content());
    p.setUserAccount(CurrentUser.account());
    p.setCreateTime(Instant.now());
    return ApiResult.ok(forumPostRepository.save(p));
  }
}