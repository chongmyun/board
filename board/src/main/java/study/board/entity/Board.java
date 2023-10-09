package study.board.entity;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import study.board.board.dto.BoardModifyDto;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//@SQLDelete(sql = "UPDATE board SET deleted_date = NOW() WHERE board_id = ? AND deleted_date IS NULL" ) -> 연관관계에 있는 데이터가 삭제될 수 있음 사용 x
@Where(clause = "deleted_date IS NULL")
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

  @OneToMany
  @JoinColumn(name = "board_id")
  private List<BoardFiles> boardFiles = new ArrayList<>();

  private String title;

  @Lob
  private String content;

  private int viewCount;

  @OneToOne(mappedBy = "board", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
  private BoardDetail boardDetail;

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

  public void addViewCount(int viewCount){
    this.viewCount = viewCount;
  }

  public void addComment(BoardComment boardComment){
    this.boardComments.add(boardComment);
  }

  public void addBoardFiles(BoardFiles boardFiles){
    this.boardFiles.add(boardFiles);
  }

  public void addBoardDetail(BoardDetail detail) {
    this.boardDetail = detail;
    detail.setBoard(this);
  }


}
