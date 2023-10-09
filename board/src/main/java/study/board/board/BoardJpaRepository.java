package study.board.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.board.entity.Board;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long>,BoardRepository {

  Page<Board> findAllBy(Pageable pageable);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE Board b SET b.deletedDate = CURRENT_TIMESTAMP WHERE b.id = :boardId AND b.deletedDate IS NULL")
  void softDeleteBoard(@Param("boardId") Long boardId);
}
