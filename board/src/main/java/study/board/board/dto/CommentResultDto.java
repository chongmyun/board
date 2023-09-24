package study.board.board.dto;

import lombok.Getter;
import lombok.ToString;
import study.board.entity.BoardComment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class CommentResultDto {

  private Long commentId;

  private String content;

  private String userId;

  private List<CommentResultDto> child = new ArrayList<>();

  public CommentResultDto(Long commentId, String content, String userId,  List<CommentResultDto> child) {
    this.commentId = commentId;
    this.content = content;
    this.userId = userId;
    this.child = child;
  }

  public static CommentResultDto of(BoardComment boardComment) {
    return new CommentResultDto(
        boardComment.getId(),
        boardComment.getContent(),
        boardComment.getMember().getUserId(),
        boardComment.getChild().stream().map(CommentResultDto::of).collect(Collectors.toList())
    );
  }
}
