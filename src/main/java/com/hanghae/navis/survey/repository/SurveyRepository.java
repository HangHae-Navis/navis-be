package com.hanghae.navis.survey.repository;

import com.hanghae.navis.survey.dto.AdminSurveyGetDto;
import com.hanghae.navis.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    @Query(value = "SELECT bb.id AS boardId, bb.title AS title, " +
            "bb.created_at AS createdAt, " +
            "sq.type AS type, sq.id AS questionId, " +
            "sq.number AS questionNumber, sq.question AS question, " +
            "GROUP_CONCAT(DISTINCT so.option ORDER BY so.id SEPARATOR ', ') AS options, " +
            "GROUP_CONCAT(IFNULL( " +
            "(SELECT " +
            "IF(sq.type = 'DESCRIPTIVE', GROUP_CONCAT(DISTINCT a.answer ORDER BY so.id SEPARATOR '|| '), COUNT(a.id)) " +
            "FROM answer a " +
            "WHERE a.survey_question_id = sq.id AND (sq.type = 'DESCRIPTIVE' OR a.survey_option_id = so.id) " +
            "), 'None'" +
            ") SEPARATOR ', ') AS answerCount " +
            "FROM navis.basic_board bb " +
            "LEFT OUTER JOIN survey_question sq ON sq.survey_id = bb.id " +
            "LEFT OUTER JOIN survey_option so ON so.survey_question_id = sq.id " +
            "WHERE bb.dtype = 'survey' AND bb.id = :boardId " +
            "GROUP BY bb.id, sq.id", nativeQuery = true)
    List<AdminSurveyGetDto> findAnswerListByBasicBoardId(@Param("boardId") Long boardId);
}
