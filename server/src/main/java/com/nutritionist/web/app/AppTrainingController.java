package com.nutritionist.web.app;

import com.nutritionist.common.ApiResult;
import com.nutritionist.domain.Training;
import com.nutritionist.repo.TrainingRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app/trainings")
public class AppTrainingController {

    private final TrainingRepository trainingRepository;

    public AppTrainingController(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @GetMapping
    public ApiResult<List<Training>> list() {
        return ApiResult.ok(trainingRepository.findAll());
    }

    @GetMapping("/{id}")
    public ApiResult<Training> getById(@PathVariable String id) {
        return trainingRepository.findById(id)
                .map(ApiResult::ok)
                .orElse(ApiResult.error("实训不存在"));
    }
}