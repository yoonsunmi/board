package com.example.board.service;

import com.example.board.dto.response.BoardPageResponse;
import com.example.board.dto.response.BoardResponse;
import com.example.board.entity.Board;
import com.example.board.enums.ErrorCode;
import com.example.board.exception.BoardException;
import com.example.board.repository.BoardQueryRepository;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardQueryRepository boardQueryRepository;

    public BoardPageResponse getList(int page, int size) {
        if (page < 0 || size < 1) {
            throw new BoardException(ErrorCode.PAGE_NOT_FOUND);
        }

        long totalCount = boardQueryRepository.count();
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // 첫 페이지가 아니면서 전체 페이지 보다 요청 페이지가 같거나 큰 경우
        if (page > 0 && page >= totalPages) {
            throw new BoardException(ErrorCode.PAGE_NOT_FOUND);
        }

        List<BoardResponse> result = boardQueryRepository.findAll(page, size).stream()
                .map(BoardResponse::from)
                .toList();

        return BoardPageResponse.builder()
                .content(result)
                .page(page)
                .totalPages(totalPages)
                .firstPage(page == 0)
                .lastPage(page >= totalPages - 1)
                .build();
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

    public BoardResponse getDetail(Long id) {
        Board board = boardQueryRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorCode.BOARD_NOT_FOUND));

        return BoardResponse.from(board);
    }

    @Transactional
    public void update(Long id, String title, String writer, String content) {
        Board board = findBoard(id);
        board.update(title, writer, content);
    }

    @Transactional
    public void delete(Long id) {
        Board board = findBoard(id);
        boardRepository.delete(board);
    }

    private Board findBoard(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BoardException(ErrorCode.BOARD_NOT_FOUND));
    }

}
