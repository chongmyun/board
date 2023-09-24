package study.board.board.dto;

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
}
