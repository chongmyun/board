package study.board.board.view;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.BoardView;

public interface BoardViewJpaRepository extends JpaRepository<BoardView,Long> {
  int countBoardViewByMember_IdAndBoard_Id(Long memberId,Long boardId);
}
