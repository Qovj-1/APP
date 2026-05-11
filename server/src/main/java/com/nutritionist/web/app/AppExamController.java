package com.nutritionist.web.app;

import org.springframework.beans.factory.annotation.Autowired;
import com.nutritionist.common.ApiResult;
import com.nutritionist.common.BusinessException;
import com.nutritionist.domain.ExamPaper;
import com.nutritionist.domain.ExamScore;
import com.nutritionist.domain.Exercise;
import com.nutritionist.domain.MockQuestion;
import com.nutritionist.domain.RealQuestion;
import com.nutritionist.repo.ExamPaperRepository;
import com.nutritionist.repo.ExamScoreRepository;
import com.nutritionist.repo.ExerciseRepository;
import com.nutritionist.repo.MockQuestionRepository;
import com.nutritionist.repo.RealQuestionRepository;
import com.nutritionist.support.CurrentUser;
import com.nutritionist.web.dto.ExamResultResponse;
import com.nutritionist.web.dto.ExamSubmitRequest;
import com.nutritionist.web.dto.QuestionResult;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/app/exams")
public class AppExamController {

  @Autowired private ExamPaperRepository examPaperRepository;
  @Autowired private ExamScoreRepository examScoreRepository;
  @Autowired private ExerciseRepository exerciseRepository;
  @Autowired private MockQuestionRepository mockQuestionRepository;
  @Autowired private RealQuestionRepository realQuestionRepository;

  @GetMapping("/papers")
  public ApiResult<List<ExamPaper>> papers() {
    return ApiResult.ok(examPaperRepository.findAll());
  }

  @GetMapping("/papers/{id}/questions")
  public ApiResult<List<Map<String, Object>>> paperQuestions(@PathVariable Integer id) {
    ExamPaper paper = examPaperRepository.findById(id)
        .orElseThrow(() -> new BusinessException("试卷不存在"));
    
    List<Map<String, Object>> questions = new ArrayList<>();
    
    List<Exercise> exercises = exerciseRepository.findAll().stream().limit(2).collect(Collectors.toList());
    List<MockQuestion> mockQuestions = mockQuestionRepository.findAll().stream().limit(1).collect(Collectors.toList());
    List<RealQuestion> realQuestions = realQuestionRepository.findAll().stream().limit(1).collect(Collectors.toList());
    
    int index = 1;
    for (Exercise ex : exercises) {
      Map<String, Object> q = new java.util.HashMap<>();
      q.put("id", String.valueOf(index++));
      q.put("type", "EXERCISE");
      q.put("title", ex.getTitle());
      q.put("options", Arrays.asList(
        Map.of("key", "A", "text", ex.getOptionA()),
        Map.of("key", "B", "text", ex.getOptionB()),
        Map.of("key", "C", "text", ex.getOptionC()),
        Map.of("key", "D", "text", ex.getOptionD())
      ));
      questions.add(q);
    }
    
    for (MockQuestion mq : mockQuestions) {
      Map<String, Object> q = new java.util.HashMap<>();
      q.put("id", String.valueOf(index++));
      q.put("type", "MULTI");
      q.put("title", mq.getTitle());
      q.put("options", Arrays.asList(
        Map.of("key", "A", "text", mq.getOptionA()),
        Map.of("key", "B", "text", mq.getOptionB()),
        Map.of("key", "C", "text", mq.getOptionC()),
        Map.of("key", "D", "text", mq.getOptionD())
      ));
      questions.add(q);
    }
    
    for (RealQuestion rq : realQuestions) {
      Map<String, Object> q = new java.util.HashMap<>();
      q.put("id", String.valueOf(index++));
      q.put("type", "CASE");
      q.put("title", rq.getTitle());
      questions.add(q);
    }
    
    return ApiResult.ok(questions);
  }

  @PostMapping("/papers/{id}/submit")
  public ApiResult<ExamResultResponse> submit(@PathVariable Integer id, @Valid @RequestBody ExamSubmitRequest req) {
    ExamPaper paper = examPaperRepository.findById(id)
        .orElseThrow(() -> new BusinessException("试卷不存在"));
    Map<String, String> answers = req.answers() != null ? req.answers() : Map.of();
    String account = CurrentUser.account();
    
    List<QuestionResult> questionResults = new ArrayList<>();
    int correct = 0;
    
    List<Exercise> exercises = exerciseRepository.findAll().stream().limit(2).collect(Collectors.toList());
    List<MockQuestion> mockQuestions = mockQuestionRepository.findAll().stream().limit(1).collect(Collectors.toList());
    List<RealQuestion> realQuestions = realQuestionRepository.findAll().stream().limit(1).collect(Collectors.toList());
    
    int index = 1;
    for (Exercise ex : exercises) {
      String qId = String.valueOf(index++);
      String userAnswer = answers.getOrDefault(qId, "").trim().toUpperCase();
      boolean isCorrect = ex.getAnswer().equals(userAnswer);
      if (isCorrect) correct++;
      
      questionResults.add(new QuestionResult(
        qId,
        "EXERCISE",
        ex.getTitle(),
        userAnswer,
        ex.getAnswer(),
        isCorrect,
        ex.getAnalysis()
      ));
    }
    
    for (MockQuestion mq : mockQuestions) {
      String qId = String.valueOf(index++);
      String userAnswer = answers.getOrDefault(qId, "").trim().toUpperCase();
      
      Set<String> userSet = new HashSet<>(Arrays.asList(userAnswer.split(",")));
      Set<String> correctSet = new HashSet<>(Arrays.asList(mq.getAnswer().split(",")));
      boolean isCorrect = userSet.equals(correctSet);
      if (isCorrect) correct++;
      
      questionResults.add(new QuestionResult(
        qId,
        "MULTI",
        mq.getTitle(),
        userAnswer,
        mq.getAnswer(),
        isCorrect,
        mq.getAnalysis()
      ));
    }
    
    for (RealQuestion rq : realQuestions) {
      String qId = String.valueOf(index++);
      String userAnswer = answers.getOrDefault(qId, "").trim();
      boolean isCorrect = userAnswer.length() > 10;
      if (isCorrect) correct++;
      
      questionResults.add(new QuestionResult(
        qId,
        "CASE",
        rq.getTitle(),
        userAnswer,
        rq.getAnswer(),
        isCorrect,
        rq.getAnalysis()
      ));
    }
    
    int total = questionResults.size();
    int score = total == 0 ? 0 : Math.round(100f * correct / total);
    
    ExamScore sc = new ExamScore();
    sc.setName(account);
    sc.setPaperId(id);
    sc.setExamTime(Instant.now());
    sc.setScore(score);
    examScoreRepository.save(sc);
    
    return ApiResult.ok(new ExamResultResponse(score, total, correct, questionResults));
  }

  @GetMapping("/scores")
  public ApiResult<List<ExamScore>> myScores() {
    return ApiResult.ok(examScoreRepository.findByNameOrderByExamTimeDesc(CurrentUser.account()));
  }
}