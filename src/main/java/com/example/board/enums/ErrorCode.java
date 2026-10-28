package com.example.board.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 페이지입니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다.");

    private final HttpStatus status;
    private final String message;

}
