package study.board.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.entity.Member;
import study.board.member.dto.MemberDto;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberJpaRepository memberRepository;
    /**
     * 1. 회원가입
     * */
    public void saveMember(MemberDto memberDto){

        Member overlapUser = memberRepository.findByUserId(memberDto.getUserId()).orElse(null);
        if(overlapUser != null){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Member saveMember = Member.builder().userId(memberDto.getUserId()).password(memberDto.getPassword())
                .name(memberDto.getName()).build();

        memberRepository.save(saveMember);
    }

    public MemberDto login(MemberDto memberDto){
        Member member = memberRepository.findByUserId(memberDto.getUserId())
                .orElseThrow(() ->  new IllegalArgumentException("아이디 혹은 비밀번호가 일치하지 않습니다."));

        if(!member.getPassword().equals(memberDto.getPassword())){
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 일치하지 않습니다.");
        }

        return MemberDto.builder().memberId(member.getId()).userId(member.getUserId())
                .name(member.getName()).build();
    }


}
