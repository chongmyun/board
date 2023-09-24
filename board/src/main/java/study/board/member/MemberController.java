package study.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.board.member.dto.MemberDto;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> signHandlingException(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @PostMapping("/member/save")
    public ResponseEntity<String> saveMember(@RequestBody MemberDto memberDto){
        memberService.saveMember(memberDto);
        return ResponseEntity.ok("회원가입 하였습니다.");
    }

    @PostMapping("/member/login")
    public ResponseEntity<MemberDto> loginMember(@RequestBody MemberDto memberDto){
        return ResponseEntity.ok(memberService.login(memberDto));
    }



}
