package com.example.board.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardPageResponse {

    private final List<BoardResponse> content;
    private final int page;
    private final int totalPages;
    private final boolean firstPage;
    private final boolean lastPage;

}
