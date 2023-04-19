package com.hanghae.navis.homework.repository;

import com.hanghae.navis.homework.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
