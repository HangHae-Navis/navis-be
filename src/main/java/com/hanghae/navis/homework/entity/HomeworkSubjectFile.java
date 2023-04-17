package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
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

    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne
    private HomeworkSubject homeworkSubject;

    public HomeworkSubjectFile(String fileUrl, String fileName, HomeworkSubject homeworkSubject) {
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.homeworkSubject = homeworkSubject;
    }
}
