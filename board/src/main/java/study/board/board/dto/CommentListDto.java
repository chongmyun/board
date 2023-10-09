package study.board.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;
import study.board.entity.BoardComment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class CommentListDto {

  private Long commentId;

  private Long parentId;

  private String content;

  private String userId;

  private List<CommentListDto> child = new ArrayList<>();

  @QueryProjection
  public CommentListDto(Long commentId,Long parentId, String content, String userId) {
    this.commentId = commentId;
    this.parentId = parentId;
    this.content = content;
    this.userId = userId;
  }

  public CommentListDto(Long commentId, String content, String userId, List<CommentListDto> child) {
    this.commentId = commentId;
    this.content = content;
    this.userId = userId;
    this.child = child;
  }

  public static CommentListDto of(BoardComment boardComment) {
    return new CommentListDto(
        boardComment.getId(),
        boardComment.getContent(),
        boardComment.getMember().getUserId(),
        boardComment.getChild().stream().map(CommentListDto::of).collect(Collectors.toList())
    );
  }
}
