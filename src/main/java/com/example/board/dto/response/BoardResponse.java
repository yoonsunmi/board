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
        return BoardResponse.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .createdAt(board.getCreatedAt())
                .modifiedAt(board.getModifiedAt())
                .build();
    }

}
