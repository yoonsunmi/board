package com.example.board.dto.response;

import com.example.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardResponse {
    private final Long id;
    private final String title;
    private final String writer;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    // entity -> dto
    public static BoardResponse from(Board board) {
        return new BoardResponse(board.getId(), board.getTitle(), board.getWriter(), board.getContent(),
                board.getCreatedAt(), board.getModifiedAt());
    }

}
