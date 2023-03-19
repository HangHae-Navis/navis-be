package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "homework_subject_file")
@Getter
@NoArgsConstructor
public class HomeworkSubjectFile extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne
    private HomeworkSubject homeworkSubject;

    public HomeworkSubjectFile(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
