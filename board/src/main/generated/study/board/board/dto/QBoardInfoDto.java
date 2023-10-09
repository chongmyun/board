package study.board.board.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * study.board.board.dto.QBoardInfoDto is a Querydsl Projection type for BoardInfoDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QBoardInfoDto extends ConstructorExpression<BoardInfoDto> {

    private static final long serialVersionUID = -985714577L;

    public QBoardInfoDto(com.querydsl.core.types.Expression<Long> boardId, com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> userId, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdDate, com.querydsl.core.types.Expression<Integer> viewCount, com.querydsl.core.types.Expression<Integer> commentCount) {
        super(BoardInfoDto.class, new Class<?>[]{long.class, long.class, String.class, String.class, String.class, java.time.LocalDateTime.class, int.class, int.class}, boardId, memberId, userId, title, content, createdDate, viewCount, commentCount);
    }

}

