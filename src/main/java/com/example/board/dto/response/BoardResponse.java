package com.example.board.dto.response;

import com.example.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponse {
    private final Long id;
    private final String title;
    private final String writer;
    private final String content;

    // entity -> dto
    public static BoardResponse from(Board board) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getWriter(), board.getContent());
    }

}
