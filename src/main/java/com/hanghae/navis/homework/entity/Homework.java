package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.Group;
import com.hanghae.navis.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "homework")
@Getter
@NoArgsConstructor
public class Homework extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Group group;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "homework")
    List<HomeworkComment> homeworkCommentList = new ArrayList<>();
}
