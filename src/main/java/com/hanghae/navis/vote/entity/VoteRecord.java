package com.hanghae.navis.vote.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.GroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "voteRecord")
@Getter
@NoArgsConstructor
public class VoteRecord extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private VoteOption voteOption;

    @ManyToOne
    @JoinColumn(name = "groupmember_id")
    private GroupMember groupMember;
}
