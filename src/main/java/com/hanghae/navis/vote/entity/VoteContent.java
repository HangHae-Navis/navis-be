package com.hanghae.navis.vote.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "voteContent")
@Getter
@NoArgsConstructor
public class VoteContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Vote vote;
    private String option;

    @OneToMany(mappedBy = "voteContent")
    private List<VoteRecord> voteRecordList;
}
