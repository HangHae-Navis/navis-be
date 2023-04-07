package com.hanghae.navis.survey.repository;

import com.hanghae.navis.survey.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
}
