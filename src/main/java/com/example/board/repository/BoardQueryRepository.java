package com.example.board.repository;

import com.example.board.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.board.entity.QBoard.board;

@Repository
@RequiredArgsConstructor
public class BoardQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Board> findAll(int page, int size) {
        return jpaQueryFactory
                .selectFrom(board)
                .orderBy(board.id.desc())
                .offset((long) page * size)
                .limit(size)
                .fetch();
    }

    public long count() {
        Long count = jpaQueryFactory
                .select(board.count())
                .from(board)
                .fetchOne();

        return count == null ? 0 : count;
    }

}
