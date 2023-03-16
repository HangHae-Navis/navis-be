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

    @OneToMany(mappedBy = "voteOption", cascade = {CascadeType.ALL})
    private List<VoteRecord> voteRecordList;

    public VoteOption(Vote vote, String option) {
        this.vote = vote;
        this.option = option;
    }

    public VoteOption(String option) {
        this.option = option;
    }
}
