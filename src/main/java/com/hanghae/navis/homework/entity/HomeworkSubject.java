package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "homework_subject")
@Getter
@NoArgsConstructor
public class HomeworkSubject extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean submit;
    private boolean late;
    private boolean submitCheck;
    @ManyToOne
    private User user;
    @ManyToOne
    private Group group;
    @ManyToOne
    private Homework homework;

    @OneToMany(mappedBy = "homeworkSubject", cascade = {CascadeType.ALL})
    List<HomeworkSubjectFile> homeworkSubjectFileList = new ArrayList<>();

    @OneToMany(mappedBy = "homeworkSubject", cascade = {CascadeType.ALL})
    List<Feedback> feedbackList = new ArrayList<>();

    public HomeworkSubject(boolean submit, boolean late, boolean submitCheck, User user, Group group, Homework homework) {
        this.submit = submit;
        this.late = late;
        this.submitCheck = submitCheck;
        this.user = user;
        this.group = group;
        this.homework = homework;
    }

    public void submitCheck(boolean submitCheck) {
        this.submitCheck = submitCheck;
    }
}
