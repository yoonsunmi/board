package com.example.board.service;

import com.example.board.dto.response.BoardResponse;
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

    public Page<BoardResponse> getList(Pageable pageable) {
        return boardRepository.findAll(pageable)
                .map(BoardResponse::from);
    }

}
