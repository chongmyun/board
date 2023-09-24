package study.board.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.board.entity.Board;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long>,BoardRepository {

  Page<Board> findAllBy(Pageable pageable);
}
