package com.hanghae.navis.survey.repository;

import com.hanghae.navis.group.entity.GroupMember;
import com.hanghae.navis.survey.dto.SubmitAnswerResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubmitAnswerRepository extends JpaRepository<GroupMember, Long> {
}