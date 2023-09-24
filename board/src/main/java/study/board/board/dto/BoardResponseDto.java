package study.board.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import study.board.entity.Board;
import study.board.entity.Member;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class BoardResponseDto {

    @JsonIgnore
    private final DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Long boardId;
    private Long memberId;
    private String userId;
    private String title;
    private String content;
    private String createdDate;
    private int viewCount;
    private int commentCount;

    private List<CommentResultDto> comments = new ArrayList<>();

    public BoardResponseDto(Board board){
        Member member = board.getMember();
        this.boardId = board.getId();
        this.memberId = member.getId();
        this.userId = member.getUserId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.viewCount = board.getViewCount();
        if(board.getCreatedDate() != null){
            this.createdDate = board.getCreatedDate().format(dataTimeFormatter);
        }
        this.commentCount = board.getBoardComments().size();
    }

    public void addComment(List<CommentResultDto> commentResultDto){
        this.comments.addAll(commentResultDto);
    }
}
