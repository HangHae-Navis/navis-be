package com.hanghae.navis.vote.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "voteOption")
@Getter
@NoArgsConstructor
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Vote vote;
    private String option;

    @OneToMany(mappedBy = "voteOption")
    private List<VoteRecord> voteRecordList;
}
