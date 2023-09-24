package study.board.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardModifyDto {

    private Long memberId;

    private Long boardId;

    private String userId;

    private String title;

    private String content;
}
