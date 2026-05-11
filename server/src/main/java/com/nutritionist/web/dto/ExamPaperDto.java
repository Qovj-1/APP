package com.nutritionist.web.dto;

import com.nutritionist.domain.ExamPaper;
import java.math.BigDecimal;

public record ExamPaperDto(
    Integer typeId, String problem, String answer, BigDecimal difficult) {

  public static ExamPaperDto from(ExamPaper p) {
    return new ExamPaperDto(
        p.getTypeId(), p.getProblem(), p.getAnswer(), p.getDifficult());
  }
}