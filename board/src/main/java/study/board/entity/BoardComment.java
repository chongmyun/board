package study.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class BoardComment extends BaseEntity{

  @Id
  @GeneratedValue
  @Column(name = "board_comment_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id")
  private Board board;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private BoardComment parent;

  @OneToMany(mappedBy = "parent")
  private List<BoardComment> child = new ArrayList<>();

  private CommentStatus status;

  @Builder
  public BoardComment(Member member, String content,Board board,BoardComment parent,@Nullable Long id) {
    this.member = member;
    this.content = content;
    this.parent = parent;
    this.board = board;
    this.id = id;
    this.status = CommentStatus.REGISTER;
  }

  public void setParent(BoardComment parent) {
    this.parent = parent;
  }

  public void modifyContent(String content, CommentStatus status) {
    this.content = content;
    if(status != null) {
      this.status = status;
    }
  }

}
