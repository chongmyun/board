package study.board.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {

    private Long memberId;
    private String userId;
    private String password;
    private String name;

    @Builder
    public MemberDto(Long memberId, String userId, String name) {
        this.memberId = memberId;
        this.userId = userId;
        this.name = name;
    }
}
