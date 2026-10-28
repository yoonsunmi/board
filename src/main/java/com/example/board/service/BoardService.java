package com.example.board.service;

import com.example.board.entity.Board;
import com.example.board.enums.ErrorCode;
import com.example.board.exception.BoardException;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> getList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public Long create(String title, String writer, String content) {
        Board board = Board.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();

        return boardRepository.save(board).getId();
    }

    public Board getDetail(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Transactional
    public void update(Long id, String title, String writer, String content) {
        Board board = getDetail(id);
        board.update(title, writer, content);
    }

}
