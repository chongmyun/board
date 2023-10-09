package study.board.board.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * study.board.board.dto.QCommentListDto is a Querydsl Projection type for CommentListDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentListDto extends ConstructorExpression<CommentListDto> {

    private static final long serialVersionUID = 844762086L;

    public QCommentListDto(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<Long> parentId, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<String> userId) {
        super(CommentListDto.class, new Class<?>[]{long.class, long.class, String.class, String.class}, commentId, parentId, content, userId);
    }

}

