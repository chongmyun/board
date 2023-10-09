package study.board.board.cache;

import study.board.board.dto.BoardInfoDto;
import study.board.entity.Board;

public interface BoardCaching {

  BoardInfoDto showBoard(Long boardId, Long memberId);

  Integer updateViewCount(Long boardId);
}
