package study.board.board;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import study.board.board.dto.BoardModifyDto;
import study.board.entity.Board;
import study.board.entity.BoardComment;
import study.board.entity.Member;
import study.board.member.MemberJpaRepository;

import java.awt.print.Pageable;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BoardJpaRepositoryTest {

  @Autowired
  private BoardJpaRepository boardRepository;

  @Autowired
  private MemberJpaRepository memberRepository;

  @Autowired
  private BoardCommentJpaRepository boardCommentRepository;

  @Autowired
  private EntityManager em;

  @BeforeEach
  public void init(){
    //회원 입력
    Member member = Member.builder().name("member1").password("1234").userId("chdaud33").build();
    memberRepository.save(member);

  }

  @Test
  @DisplayName("게시글저장")
  void saveBoard(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("수술실 CCTV 의무화").content("수술실 CCTV 의무화에 대한 내용입니다.").member(member).build();
    //when
    Board saveBoard = boardRepository.save(board);
    //then
    assertThat(saveBoard).isNotNull();
    assertThat(saveBoard.getMember()).isEqualTo(member);
    assertThat(saveBoard.getTitle()).isEqualTo("수술실 CCTV 의무화");
  }

  @Test
  @DisplayName("게시글 수정")
  void updateBoard(){
    em.flush();
    em.clear();
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.")
            .member(member).build();
    boardRepository.save(board);

    BoardModifyDto boardModifyDto = new BoardModifyDto();
    boardModifyDto.setBoardId(1L);
    boardModifyDto.setMemberId(1L);
    boardModifyDto.setTitle("제목 : 게시글 수정");
    boardModifyDto.setContent("게시글 수정 내용입니다.");
    //when
    Board findBoard = boardRepository.findById(board.getId()).get();
    findBoard.updateBoard(boardModifyDto);

    //then
    assertThat(findBoard.getTitle()).isEqualTo(boardModifyDto.getTitle());
    assertThat(findBoard.getContent()).isEqualTo(boardModifyDto.getContent());
  }

  @Test
  @DisplayName("게시글 삭제")
  void deleteBoard(){
    Member member = memberRepository.findByUserId("chdaud33").get();

    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.")
            .member(member).build();
    boardRepository.save(board);
    em.flush();
    em.clear();
    //given
    boardRepository.deleteById(1L);
    //when
    Board findBoard = boardRepository.findById(1L).orElseGet(() -> null);
    //then
    assertThat(findBoard).isNull();
  }

  @Test
  @DisplayName("게시글 조회")
  void getBoards(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    for(int i = 0 ; i<10 ; i++){
      Board board = Board.builder().title("아파트 주차비 논란"+i).content("아파트 주차비 논란에 대한 내용입니다."+i).member(member).build();
      boardRepository.save(board);
    }

    //when
    PageRequest pageRequest = PageRequest.of(0,10);
    Page<Board> boards = boardRepository.findAllBy(pageRequest);
    //then

    assertThat(boards.getTotalElements()).isEqualTo(10);
    assertThat(boards.getTotalPages()).isEqualTo(1);
    assertThat(boards.getContent().get(0).getTitle()).isEqualTo("아파트 주차비 논란0");

  }

  @Test
  @DisplayName("게시글 상세보기")
  void getBoardInfo(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).build();
    boardRepository.save(board);
    //when
    Board findBoard = boardRepository.findById(board.getId()).orElse(null);
    //then
    assertThat(findBoard).isNotNull();
    assertThat(findBoard.getTitle()).isEqualTo("아파트 주차비 논란");
    assertThat(findBoard.getContent()).isEqualTo("아파트 주차비 논란에 대한 내용입니다.");
  }

  @Test
  @DisplayName("댓글 달기")
  void saveComment(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).build();
    boardRepository.save(board);

    BoardComment comment = BoardComment.builder().content("댓글입니다.").member(member).board(board).build();
    boardCommentRepository.save(comment);
    //when
    BoardComment boardComment = boardCommentRepository.findById(comment.getId()).orElse(null);
    //then
    assertThat(boardComment).isNotNull();
    assertThat(boardComment.getContent()).isEqualTo("댓글입니다.");
    assertThat(boardComment.getBoard().getId()).isEqualTo(board.getId());
    assertThat(boardComment.getMember().getId()).isEqualTo(member.getId());
  }

  @Test
  @DisplayName("댓글 수정")
  void updateComment(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).build();
    boardRepository.save(board);
    BoardComment comment = BoardComment.builder().content("댓글입니다.").member(member).board(board).build();
    boardCommentRepository.save(comment);

    em.flush();
    em.clear();
    BoardComment boardComment = boardCommentRepository.findById(comment.getId()).orElse(null);
    //when
    boardComment.modifyContent("댓글 수정입니다.",null);
    //then
    assertThat(boardComment).isNotNull();
    assertThat(boardComment.getContent()).isEqualTo("댓글 수정입니다.");
    assertThat(boardComment.getBoard().getId()).isEqualTo(board.getId());
    assertThat(boardComment.getMember().getId()).isEqualTo(member.getId());
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment(){
    //given
    Member member = memberRepository.findByUserId("chdaud33").get();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).build();
    boardRepository.save(board);
    BoardComment comment = BoardComment.builder().content("댓글입니다.").member(member).board(board).build();
    boardCommentRepository.save(comment);
    //when
    boardCommentRepository.deleteById(comment.getId());
    //then
    assertThat(comment).isNotNull();

  }

}