package com.hanghae.navis.homework.repository;

import com.hanghae.navis.homework.entity.HomeworkSubjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkSubjectFileRepository extends JpaRepository<HomeworkSubjectFile, Long> {
    List<HomeworkSubjectFile> findFileUrlByHomeworkSubjectId(Long subjectId);
}
