package study.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor
@OnDelete(action = OnDeleteAction.CASCADE)
public class BoardDetail {

  @Id
  @GeneratedValue
  @Column(name = "board_detail_id")
  private Long id;

  private String detailContent;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @Builder
  public BoardDetail(String detailContent) {
    this.detailContent = detailContent;
  }

  public void setBoard(Board board){
    this.board = board;
  }

}
