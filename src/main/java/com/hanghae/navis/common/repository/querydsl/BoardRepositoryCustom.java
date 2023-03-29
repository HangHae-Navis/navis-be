package com.hanghae.navis.common.repository.querydsl;

import com.hanghae.navis.board.entity.Board;

public interface BoardRepositoryCustom {
    Board findById(Long boardId);
}
