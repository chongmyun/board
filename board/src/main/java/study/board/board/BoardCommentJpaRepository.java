package study.board.board;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.BoardComment;

import java.util.List;

public interface BoardCommentJpaRepository extends JpaRepository<BoardComment,Long> {
  List<BoardComment> findAllByBoardIdAndParentIsNull(Long boardId);

  List<BoardComment> findAllByBoardId(Long boardId);;
}
