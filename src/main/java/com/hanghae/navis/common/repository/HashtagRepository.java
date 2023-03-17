package com.hanghae.navis.common.repository;

import com.hanghae.navis.common.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HashtagRepository extends JpaRepository <Hashtag, Long> {
    List<Hashtag> findAllByBasicBoardId(Long boardId);
}
