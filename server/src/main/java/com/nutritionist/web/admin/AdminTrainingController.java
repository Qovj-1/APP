package com.nutritionist.web.admin;

import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.Training;
import com.nutritionist.repo.TrainingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/trainings")
public class AdminTrainingController {

    private final TrainingRepository trainingRepository;

    public AdminTrainingController(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @GetMapping
    public ApiResult<Page<Training>> list(@RequestParam(defaultValue = "0") int page, 
                                          @RequestParam(defaultValue = "") String keyword) {
        Page<Training> result;
        if (!keyword.isEmpty()) {
            result = trainingRepository.findByNameContainingIgnoreCase(keyword, PageRequest.of(page, 10));
        } else {
            result = trainingRepository.findAll(PageRequest.of(page, 10));
        }
        return ApiResult.success(result);
    }

    @GetMapping("/{id}")
    public ApiResult<Training> getById(@PathVariable String id) {
        return trainingRepository.findById(id)
                .map(ApiResult::success)
                .orElse(ApiResult.error("实训不存在"));
    }

    @PostMapping
    public ApiResult<Training> create(@RequestBody Training training) {
        if (training.getTrainingId() == null || training.getTrainingId().isEmpty()) {
            throw new BusinessException("分类号不能为空");
        }
        if (training.getName() == null || training.getName().isEmpty()) {
            throw new BusinessException("项目名不能为空");
        }
        if (training.getContent() == null || training.getContent().isEmpty()) {
            throw new BusinessException("内容不能为空");
        }
        Training saved = trainingRepository.save(training);
        return ApiResult.success(saved);
    }

    @PutMapping("/{id}")
    public ApiResult<Training> update(@PathVariable String id, @RequestBody Map<String, Object> data) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new BusinessException("实训不存在"));
        
        if (data.containsKey("name")) {
            training.setName((String) data.get("name"));
        }
        if (data.containsKey("content")) {
            training.setContent((String) data.get("content"));
        }
        if (data.containsKey("time")) {
            training.setTime(((Number) data.get("time")).shortValue());
        }
        if (data.containsKey("knowledgePoints")) {
            training.setKnowledgePoints((String) data.get("knowledgePoints"));
        }
        if (data.containsKey("difficult")) {
            training.setDifficult(((Number) data.get("difficult")).doubleValue());
        }
        
        Training saved = trainingRepository.save(training);
        return ApiResult.success(saved);
    }

    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@PathVariable String id) {
        if (!trainingRepository.existsById(id)) {
            throw new BusinessException("实训不存在");
        }
        trainingRepository.deleteById(id);
        return ApiResult.success();
    }
}