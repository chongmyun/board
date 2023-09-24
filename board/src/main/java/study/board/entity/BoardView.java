package study.board.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
public class BoardView {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToOne
  @JoinColumn(name = "board_id")
  private Board board;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Builder
  public BoardView(Board board, Member member) {
    this.board = board;
    this.member = member;
  }
}
