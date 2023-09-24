package study.board.entity;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import study.board.board.BoardController;
import study.board.board.dto.BoardModifyDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity{

  @Id
  @GeneratedValue
  @Column(name = "board_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="member_id")
  private Member member;

  @OneToMany(mappedBy = "board")
  private List<BoardComment> boardComments = new ArrayList<>();

  private String title;

  private String content;

  private int viewCount;

  @Builder
  public Board (Member member, String title ,String content,@Nullable Long id){
    this.member = member;
    this.title = title;
    this.content = content;
    this.id = id;
    this.viewCount = 0;
  }

  public void updateBoard(BoardModifyDto boardModifyDto){
    this.title = boardModifyDto.getTitle();
    this.content = boardModifyDto.getContent();
  }

  public void addComment(BoardComment boardComment){
    this.boardComments.add(boardComment);
  }

  public void addViewCount(){ this.viewCount++; }

}
