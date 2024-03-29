package com.hanghae.navis.vote.entity;

import com.hanghae.navis.common.entity.TimeStamped;
import com.hanghae.navis.group.entity.GroupMember;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity(name = "voteRecord")
@Getter
@NoArgsConstructor
public class VoteRecord extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vote vote;

    @ManyToOne
    private VoteOption voteOption;

    @ManyToOne
    private GroupMember groupMember;

    public VoteRecord(Vote vote, VoteOption voteOption, GroupMember groupMember) {
        this.vote = vote;
        this.voteOption = voteOption;
        this.groupMember = groupMember;
    }
}
