package com.hanghae.navis.homework.repository;

import com.hanghae.navis.homework.entity.HomeworkSubject;
import com.hanghae.navis.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkSubjectRepository extends JpaRepository<HomeworkSubject ,Long> {
    HomeworkSubject findByUserIdAndGroupIdAndHomeworkId(Long userId, Long groupId, Long homeworkId);

    List<HomeworkSubject> findAllByHomeworkId(Long homeworkId);
    void deleteByUser(User user);
}
