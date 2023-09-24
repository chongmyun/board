package study.board.member;

import org.springframework.data.jpa.repository.JpaRepository;
import study.board.entity.Member;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUserId(String userId);
}
