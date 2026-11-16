package com.example.board.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardPageRequest {

    private int page = 0;
    private int size = 10;

}
