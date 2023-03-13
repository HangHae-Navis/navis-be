package com.hanghae.navis.homework.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "homeworkfile")
@Getter
@NoArgsConstructor
public class HomeworkFile extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Homework homework;
    @Column(nullable = false)
    private String fileUrl;
}
