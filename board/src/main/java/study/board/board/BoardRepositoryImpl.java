package study.board.board;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import study.board.board.cond.BoardSearchCondition;
import study.board.board.dto.BoardInfoDto;
import study.board.board.dto.CommentListDto;
import study.board.board.dto.QBoardInfoDto;
import study.board.board.dto.QCommentListDto;
import study.board.entity.BoardFiles;
import study.board.entity.QBoardComment;

import java.util.List;

import static study.board.entity.QBoard.*;
import static study.board.entity.QBoardComment.*;
import static study.board.entity.QMember.member;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<BoardInfoDto> findBoardList(Pageable pageable, BoardSearchCondition condition) {
    int page = pageable.getPageNumber();
    int size = pageable.getPageSize();

    List<BoardInfoDto> boardList = queryFactory.select(new QBoardInfoDto(board.id, member.id, member.userId, board.title, board.content, board.createdDate, board.viewCount, board.boardComments.size()))
            .from(board).join(board.member, member)
            .where(writerEq(condition.getMemberId()),titleEq(condition.getTitle()))
            .orderBy(board.createdDate.desc())
            .offset((long) page * size).limit(size).fetch();

    int totalCount = queryFactory.selectFrom(board).join(board.member, member)
            .where(writerEq(condition.getMemberId()),titleEq(condition.getTitle())).fetch().size();
    return new PageImpl<BoardInfoDto>(boardList, pageable, totalCount);
  }

  @Override
  public List<CommentListDto> findCommentList(Long boardId) {
    return queryFactory.select(new QCommentListDto(boardComment.id, boardComment.parent.id,boardComment.content, boardComment.member.userId))
            .from(boardComment).join(boardComment.member, member)
            .where(boardComment.board.id.eq(boardId))
            .orderBy(boardComment.createdDate.asc())
            .fetch();
  }

  @Override
  public void saveBoardFiles(BoardFiles boardFiles) {
    em.persist(boardFiles);
  }


  private BooleanExpression titleEq(String title) {
    return title != null ? board.title.eq(title) : null;
  }

  private BooleanExpression writerEq(Long memberId) {
    return memberId != null ? member.id.eq(memberId) : null;
  }

}
