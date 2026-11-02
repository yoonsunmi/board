package com.example.board.controller;

import com.example.board.dto.request.BoardRequest;
import com.example.board.dto.response.BoardResponse;
import com.example.board.enums.ErrorCode;
import com.example.board.exception.BoardException;
import com.example.board.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    private static final int PAGE_BUTTON_COUNT = 10;

    /**
     * 게시판 목록 조회
     */
    @GetMapping({"/", "/board/list"})
    public String list(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
            , Model model
    ) {
        Page<BoardResponse> boardList = boardService.getList(pageable).map(BoardResponse::from);

        // 첫 페이지가 아니면서 전체 페이지 보다 요청 페이지가 같거나 큰 경우
        if (boardList.getNumber() > 0 && boardList.getNumber() >= boardList.getTotalPages()) {
            throw new BoardException(ErrorCode.PAGE_NOT_FOUND);
        }

        int startPage = (boardList.getNumber() / PAGE_BUTTON_COUNT) * PAGE_BUTTON_COUNT;
        int endPage = Math.min(startPage + (PAGE_BUTTON_COUNT - 1), boardList.getTotalPages() - 1);

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "index";
    }

    /**
     * 게시글 등록 화면
     */
    @GetMapping("/board/write")
    public String writeForm() {
        return "write";
    }

    /**
     * 게시글 등록
     */
    @PostMapping("/board")
    public ResponseEntity<Long> create(@RequestBody @Valid BoardRequest boardRequest) {
        Long result = boardService.create(boardRequest.getTitle(), boardRequest.getWriter(), boardRequest.getContent());

        return ResponseEntity.ok(result);
    }

    /**
     * 게시글 상세 조회
     */
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("board", BoardResponse.from(boardService.getDetail(id)));

        return "detail";
    }

    /**
     * 게시글 수정 화면
     */
    @GetMapping("/board/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", BoardResponse.from(boardService.getDetail(id)));

        return "edit";
    }

    /**
     * 게시글 수정
     */
    @PutMapping("/board/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid BoardRequest boardRequest) {
        boardService.update(id, boardRequest.getTitle(), boardRequest.getWriter(), boardRequest.getContent());

        return ResponseEntity.ok().build();
    }

    /**
     * 게시글 삭제
     */
    @DeleteMapping("/board/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boardService.delete(id);

        return ResponseEntity.ok().build();
    }

}
