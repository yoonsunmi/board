package com.example.board.dto.request;

import com.example.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 150, message = "제목은 150자 이하로 입력해주세요.")
    private String title;

    @NotBlank(message = "작성자를 입력해주세요.")
    @Size(max = 100, message = "작성자는 100자 이하로 입력해주세요.")
    private String writer;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 10000, message = "내용은 10000자 이하로 입력해주세요.")
    private String content;

    // dto -> entity
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }

}
