package com.example.board.service;

import com.example.board.dto.response.BoardPageResponse;
import com.example.board.dto.response.BoardResponse;
import com.example.board.entity.Board;
import com.example.board.enums.ErrorCode;
import com.example.board.exception.BoardException;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardPageResponse getList(int page, int size) {
        if (page < 0 || size < 1) {
            throw new BoardException(ErrorCode.PAGE_NOT_FOUND);
        }

        Page<Board> result = boardRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")));

        // 첫 페이지가 아니면서 전체 페이지 보다 요청 페이지가 같거나 큰 경우
        if (page > 0 && page >= result.getTotalPages()) {
            throw new BoardException(ErrorCode.PAGE_NOT_FOUND);
        }

        return BoardPageResponse.builder()
                .content(result.getContent().stream().map(BoardResponse::from).toList())
                .page(result.getNumber())
                .totalPages(result.getTotalPages())
                .firstPage(result.isFirst())
                .lastPage(result.isLast())
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
        return BoardResponse.from(findBoard(id));
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
