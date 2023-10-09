package study.board.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import study.board.board.cond.BoardSearchCondition;
import study.board.board.dto.BoardInfoDto;
import study.board.board.dto.BoardModifyDto;
import study.board.board.dto.CommentModifyDto;
import study.board.board.cache.BoardCaching;
import study.board.entity.Board;
import study.board.entity.BoardComment;
import study.board.entity.Member;
import study.board.member.MemberJpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

  @Mock
  BoardJpaRepository boardRepository;

  @Mock
  MemberJpaRepository memberRepository;

  @Mock
  BoardCaching boardCaching;

  @Mock
  BoardCommentJpaRepository boardCommentRepository;;

  @InjectMocks
  BoardService boardService;


  @Test
  @DisplayName("게시글 저장")
  void saveBoard(){
    //given
    // 테스트 데이터 설정
    BoardModifyDto boardModifyDto = new BoardModifyDto();
    boardModifyDto.setMemberId(2L);
    boardModifyDto.setTitle("아파트 주차비 논란");
    boardModifyDto.setContent("아파트 주차비 논란에 대한 내용입니다.");

    // Mock 객체를 사용하여 memberRepository.findById()가 호출될 때 반환할 Member 객체 생성
    Member mockMember = Member.builder().id(1L).name("김철수").userId("kim99").build();
    when(memberRepository.findById(1L)).thenReturn(Optional.of(mockMember));
    when(memberRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 회원입니다."));

    // save 메소드를 호출할 때 반환할 Board 객체 생성
    Board savedBoard = Board.builder().id(1L).title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(mockMember).build();
    when(boardRepository.save(any(Board.class))).thenReturn(savedBoard);

    //when
    // 등록되지 않은 이용자로 게시글 등록
    assertThatThrownBy(() -> boardService.saveBoard(boardModifyDto)).isInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 회원입니다.");

    // 등록된 이용자로 게시글 등록
    boardModifyDto.setMemberId(1L);
    BoardInfoDto responseDto = boardService.saveBoard(boardModifyDto);

    //then
    assertThat(responseDto).isNotNull();

    assertThat(responseDto.getTitle()).isEqualTo("아파트 주차비 논란");
    assertThat(responseDto.getContent()).isEqualTo("아파트 주차비 논란에 대한 내용입니다.");
    // memberRepository.findById()가 1번 호출되었는지 검증
    verify(memberRepository, times(2)).findById(any(Long.class));
    // boardRepository.save()가 1번 호출되었는지 검증
    verify(boardRepository, times(1)).save(any(Board.class));
  }

  @Test
  @DisplayName("게시글 수정")
  void updateBoard(){
    //given
    BoardModifyDto boardModifyDto = new BoardModifyDto();
    boardModifyDto.setMemberId(2L);
    boardModifyDto.setTitle("아파트 주차비 논란");
    boardModifyDto.setContent("아파트 주차비 논란에 대한 내용입니다.");

    //이용자 및 게시판 관련된 mock 객체 생성
    Member mockMember = Member.builder().id(1L).name("김철수").userId("kim99").build();
    when(boardRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));
    Board savedBoard = Board.builder().id(1L).title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(mockMember).build();
    when(boardRepository.findById(1L)).thenReturn(Optional.of(savedBoard));

    //when
    assertThatThrownBy(() -> boardService.updateBoard(2L,boardModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 게시글입니다.");
    assertThatThrownBy(() -> boardService.updateBoard(savedBoard.getId(),boardModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("게시글 작성자만 수정할 수 있습니다.");
    boardModifyDto.setMemberId(1L);
    BoardInfoDto boardInfoDto = boardService.updateBoard(savedBoard.getId(),boardModifyDto);
    //then

    assertThat(boardInfoDto).isNotNull();
    assertThat(boardInfoDto.getTitle()).isEqualTo("아파트 주차비 논란");
    assertThat(boardInfoDto.getContent()).isEqualTo("아파트 주차비 논란에 대한 내용입니다.");
    verify(boardRepository, times(3)).findById(any(Long.class));
  }

  @Test
  @DisplayName("게시글 삭제")
  void deleteBoard(){
    //given
    BoardModifyDto boardModifyDto = new BoardModifyDto();
    boardModifyDto.setBoardId(2L);
    boardModifyDto.setMemberId(2L);

    //이용자 및 게시판 관련된 mock 객체 생성
    Member mockMember = Member.builder().id(1L).name("김철수").userId("kim99").build();
    when(boardRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));
    Board savedBoard = Board.builder().id(1L).title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(mockMember).build();
    when(boardRepository.findById(1L)).thenReturn(Optional.of(savedBoard));

    //when
    assertThatThrownBy(() -> boardService.deleteBoard(2L,boardModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 게시글입니다.");
    boardModifyDto.setBoardId(1L);
    assertThatThrownBy(() -> boardService.deleteBoard(savedBoard.getId(),boardModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("게시글 작성자만 삭제할 수 있습니다.");
    boardModifyDto.setMemberId(1L);

    Long boardId = boardService.deleteBoard(savedBoard.getId(),boardModifyDto);

    //then
    assertThat(boardId).isEqualTo(1L);

    verify(boardRepository, times(3)).findById(any(Long.class));
  }

  @Test
  @DisplayName("게시글 목록 조회")
  void getBoards(){
    //given
    PageRequest pageable = PageRequest.of(0, 10);
    List<BoardInfoDto> boardList = new ArrayList<>();
    for(int i = 0 ; i <10 ; i++){
      Member member = Member.builder().name("김철수"+i).userId("kim99"+i).build();
      BoardInfoDto board = BoardInfoDto.builder().boardId((long) i).title("아파트 주차비 논란"+i).content("아파트 주차비 논란에 대한 내용입니다."+i).memberId(member.getId()).build();
      boardList.add(board);
    }
    BoardSearchCondition condition = new BoardSearchCondition();
    when(boardRepository.findBoardList(pageable,condition)).thenReturn(new PageImpl<>(boardList,pageable,10));
    //when
    Page<BoardInfoDto> boards = boardService.getBoards(pageable,condition);
    //then
    assertThat(boards).isNotNull();
    assertThat(boards.getTotalElements()).isEqualTo(10);
    assertThat(boards.getTotalPages()).isEqualTo(1);
    assertThat(boards.getContent().size()).isEqualTo(10);

    for(int i = 0 ; i <10 ; i++){
      assertThat(boards.getContent().get(i).getTitle()).isEqualTo("아파트 주차비 논란"+i);
      assertThat(boards.getContent().get(i).getContent()).isEqualTo("아파트 주차비 논란에 대한 내용입니다."+i);
    }

    verify(boardRepository, times(1)).findBoardList(any(Pageable.class),eq(condition));
  }

  @Test
  @DisplayName("게시글 상세 조회")
  void getBoardInfo(){
    //given
    Member member = Member.builder().name("김철수").userId("kim99").id(1L).build();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).id(1L).build();

    List<BoardComment> boardComments = new ArrayList<>();
    BoardComment parentComment = null;
    for(int i = 0 ; i< 10 ; i++){
      Member saveMember = Member.builder().name("김철수"+i).userId("kim99"+i).build();
      BoardComment comment = BoardComment.builder().member(saveMember).board(board).content("댓글입니다."+i).id((long) i).build();
      if( i%5 != 0 && i < 5)parentComment = comment;
      else if(i%5 != 0 )parentComment = comment;
      if(i == 5) parentComment = null;
      comment.setParent(parentComment);
      if(i%5 == 0)boardComments.add(comment);
    }

    //게시글 조회
    when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
    when(boardRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

    //댓글
    when(boardCommentRepository.findAllByBoardIdAndParentIsNull(board.getId())).thenReturn(boardComments);
    
    //when
    assertThatThrownBy(() -> boardService.getBoardInfo(2L,member.getId()))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 게시글입니다.");

    BoardInfoDto boardInfo = boardService.getBoardInfo(board.getId(), member.getId());
    //then
    assertThat(boardInfo).isNotNull();
    assertThat(boardInfo.getTitle()).isEqualTo("아파트 주차비 논란");
    assertThat(boardInfo.getContent()).isEqualTo("아파트 주차비 논란에 대한 내용입니다.");
    assertThat(boardInfo.getMemberId()).isEqualTo(1L);
    assertThat(boardInfo.getUserId()).isEqualTo("kim99");
  }

  @Test
  @DisplayName("댓글 입력 수정 삭제 테스트")
  void testComment(){
    //given
    Member member = Member.builder().name("김철수").userId("kim99").id(1L).build();
    Board board = Board.builder().title("아파트 주차비 논란").content("아파트 주차비 논란에 대한 내용입니다.").member(member).id(1L).build();
    List<BoardComment> boardComments = new ArrayList<>();
    BoardComment parentComment = null;
    for(int i = 0 ; i< 10 ; i++){
      Member saveMember = Member.builder().name("김철수"+i).userId("kim99"+i).build();
      BoardComment comment = BoardComment.builder().member(saveMember).board(board).content("댓글입니다."+i).id((long) i).build();
      if( i%5 != 0 && i < 5)parentComment = comment;
      else if(i%5 != 0 )parentComment = comment;
      if(i == 5) parentComment = null;
      comment.setParent(parentComment);
      if(i%5 == 0)boardComments.add(comment);
    }

    CommentModifyDto commentModifyDto = CommentModifyDto.builder().boardId(2L).memberId(1L).content("댓글입니다.").build();

    //when
    //게시글 조회
    when(boardRepository.findById(1L)).thenReturn(Optional.of(board));
    when(boardRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 게시글입니다."));

    //회원 조회
    when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
    when(memberRepository.findById(2L)).thenThrow(new IllegalArgumentException("존재하지 않는 회원입니다."));

    //then
    assertThatThrownBy(() -> boardService.saveComment(commentModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 게시글입니다.");

    commentModifyDto.setBoardId(1L);
    commentModifyDto.setMemberId(2L);
    assertThatThrownBy(() -> boardService.saveComment(commentModifyDto))
            .isExactlyInstanceOf(IllegalArgumentException.class).hasMessage("존재하지 않는 회원입니다.");

    commentModifyDto.setMemberId(1L);
    boardService.saveComment(commentModifyDto);
  }




}