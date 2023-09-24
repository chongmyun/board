package study.board.board;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.board.board.dto.CommentResultDto;
import study.board.entity.Board;
import study.board.entity.BoardComment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class JpaTest {

  @Autowired
  private BoardJpaRepository boardJpaRepository;

  @Autowired
  private BoardCommentJpaRepository boardCommentJpaRepository;

  @Autowired
  private EntityManager em;

  @Test
  void test(){
    Board board = Board.builder().title("test").content("test").build();
    boardJpaRepository.save(board);

    BoardComment root1 = BoardComment.builder().board(board).content("root1").build();
    BoardComment childOne1 = BoardComment.builder().board(board).content("childOne1").parent(root1).build();
    BoardComment childOne2 = BoardComment.builder().board(board).content("childOne2").parent(root1).build();
    BoardComment childOne3 = BoardComment.builder().board(board).content("childOne3").parent(root1).build();
    BoardComment root2 = BoardComment.builder().board(board).content("root2").build();
    BoardComment childTwo1 = BoardComment.builder().board(board).content("childTwo1").parent(root2).build();
    BoardComment childTwo2 = BoardComment.builder().board(board).content("childTwo2").parent(root2).build();
    BoardComment childTwo3 = BoardComment.builder().board(board).content("childTwo3").parent(root2).build();
    BoardComment childTwo4 = BoardComment.builder().board(board).content("childTwo4").parent(root2).build();

    board.addComment(root1);
    board.addComment(root2);

    boardCommentJpaRepository.save(root1);
    boardCommentJpaRepository.save(childOne1);
    boardCommentJpaRepository.save(childOne2);
    boardCommentJpaRepository.save(childOne3);
    boardCommentJpaRepository.save(root2);
    boardCommentJpaRepository.save(childTwo1);
    boardCommentJpaRepository.save(childTwo2);
    boardCommentJpaRepository.save(childTwo3);
    boardCommentJpaRepository.save(childTwo4);
    em.flush();
    em.clear();
    Board findBoard = boardJpaRepository.findById(board.getId()).get();

    List<BoardComment> comments = boardCommentJpaRepository.findAllByBoardIdAndParentIsNull(findBoard.getId());
    List<CommentResultDto> collect = comments.stream().map(CommentResultDto::of).collect(Collectors.toList());
    for (CommentResultDto commentResultDto : collect) {
      System.out.println("commentResultDto = " + commentResultDto);
    }

  }
}
