package study.board.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentModifyDto {

  private Long boardId;
  private Long memberId;
  private Long parentId;
  private String content;

  @Builder
  public CommentModifyDto(Long boardId, Long memberId, Long parentId, String content) {
    this.boardId = boardId;
    this.memberId = memberId;
    this.parentId = parentId;
    this.content = content;
  }
}
