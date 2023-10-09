package study.board.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import study.board.board.cond.BoardSearchCondition;
import study.board.board.dto.BoardInfoDto;
import study.board.board.dto.CommentListDto;
import study.board.entity.BoardFiles;

import java.util.List;

public interface BoardRepository {
  Page<BoardInfoDto> findBoardList(Pageable pageable, BoardSearchCondition condition);

  void saveBoardFiles(BoardFiles boardFiles);

  List<CommentListDto> findCommentList(Long boardId);
}
