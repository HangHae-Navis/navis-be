package com.hanghae.navis.survey.repository;

import com.hanghae.navis.survey.dto.SubmitResponseDto;
import com.hanghae.navis.survey.dto.UserAnswerDto;
import com.hanghae.navis.survey.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    void deleteAllByUserId(Long userId);

    List<Answer> findBySurveyIdAndUserId(Long surveyId, Long userId);

    @Query(value = "SELECT u.id as userId, u.nickname FROM users u " +
            "LEFT JOIN answer an ON an.user_id = u.id " +
            "WHERE an.survey_id = :surveyId " +
            "GROUP BY u.id ", nativeQuery = true)
    List<SubmitResponseDto> findByUserNickname(@Param("surveyId") Long surveyId);

    @Query(value = "SELECT GROUP_CONCAT(DISTINCT an.answer SEPARATOR ', ') AS answers " +
            "FROM navis.answer an " +
            "WHERE user_id = :userId AND survey_id = :surveyId GROUP BY survey_question_id", nativeQuery = true)
    List<UserAnswerDto> findByUserAnswer(@Param("userId") Long userId, @Param("surveyId") Long surveyId);
}
