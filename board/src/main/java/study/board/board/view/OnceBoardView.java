package study.board.board.view;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Board;
import study.board.entity.BoardView;
import study.board.entity.Member;
import study.board.member.MemberJpaRepository;

@Component
@Primary
@RequiredArgsConstructor
@Transactional
public class OnceBoardView implements BoardShowView {

  private final BoardViewJpaRepository boardViewJpaRepository;
  private final MemberJpaRepository memberJpaRepository;
  @Override
  public void showBoard(Board board,Long memberId) {
    if(board.getMember().getId() != memberId){
      int count = boardViewJpaRepository.countBoardViewByMember_IdAndBoard_Id(memberId,board.getId());
      if(count == 0){
        Member member = memberJpaRepository.findById(memberId).get();
        BoardView boardView = BoardView.builder().board(board).member(member).build();
        boardViewJpaRepository.save(boardView);
        board.addViewCount();
      }
    }
  }

}
