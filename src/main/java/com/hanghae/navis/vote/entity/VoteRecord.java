package com.hanghae.navis.vote.entity;

import com.hanghae.navis.group.entity.UserGroupList;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "voteRecord")
@Getter
@NoArgsConstructor
public class VoteRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VoteContent voteContent;

    @ManyToOne
    private UserGroupList userGroupList;
}
