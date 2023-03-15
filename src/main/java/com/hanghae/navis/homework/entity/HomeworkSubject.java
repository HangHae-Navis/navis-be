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
    @ManyToOne
    private User user;
    @ManyToOne
    private Group group;

    @OneToMany(mappedBy = "homework_subject")
    List<HomeworkSubjectFile> homeworkSubjectFileList = new ArrayList<>();
}
