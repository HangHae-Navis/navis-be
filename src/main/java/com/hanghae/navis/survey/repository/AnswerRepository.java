package com.hanghae.navis.survey.repository;

import com.hanghae.navis.survey.dto.AnswerDto;
import com.hanghae.navis.survey.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    void deleteAllByUserId(Long userId);

    List<Answer> findBySurveyIdAndUserId(Long surveyId, Long userId);

//    List<Answer> findAllBySurveyIdAndSurveyQuestionId(Long surveyId, Long questionId);
    List<Answer> findBySurveyId(Long surveyId);
}
