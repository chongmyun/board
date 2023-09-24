package study.board.board.view;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import study.board.entity.Board;

import java.util.Objects;

@Component
public class InfiniteBoardView implements BoardShowView {
  @Override
  public void showBoard(Board board,Long memberId) {
    if(!Objects.equals(board.getMember().getId(), memberId)){
      board.addViewCount();
    }
  }
}
