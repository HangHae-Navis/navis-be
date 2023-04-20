package com.hanghae.navis.group.entity;

import com.hanghae.navis.common.entity.BasicBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "recently_viewed")
public class RecentlyViewed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private GroupMember groupMember;
    @ManyToOne
    private BasicBoard basicBoard;

    public RecentlyViewed(GroupMember groupMember, BasicBoard basicBoard) {
        this.groupMember = groupMember;
        this.basicBoard = basicBoard;
    }

}
