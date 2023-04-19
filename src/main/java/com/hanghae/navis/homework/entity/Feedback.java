package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "feedback")
@Getter
@NoArgsConstructor
public class Feedback extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String feedback;

    @ManyToOne
    private HomeworkSubject homeworkSubject;

    public Feedback(String feedback, HomeworkSubject homeworkSubject) {
        this.feedback = feedback;
        this.homeworkSubject = homeworkSubject;
    }
}
