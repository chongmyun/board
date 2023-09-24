package study.board.board;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.board.board.dto.BoardModifyDto;
import study.board.board.dto.BoardResponseDto;
import study.board.board.dto.CommentModifyDto;
import study.board.board.dto.CommentResultDto;
import study.board.board.view.BoardShowView;
import study.board.entity.Board;
import study.board.entity.BoardComment;
import study.board.entity.CommentStatus;
import study.board.entity.Member;
import study.board.member.MemberJpaRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardJpaRepository boardRepository;

    private final BoardCommentJpaRepository boardCommentRepository;

    private final MemberJpaRepository memberRepository;

    private final BoardShowView boardShowView;

    @PostConstruct
    public void init(){
        Member member = Member.builder().userId("chdaud33").password("1234").name("정총명").build();
        memberRepository.save(member);
        for(int i= 0 ; i < 100 ; i++){
            Board board = Board.builder().member(member).title("제목" + i).content("내용" + i).build();
            boardRepository.save(board);
        }
    }

    /**
     * 게시글 저장
     * */
    public BoardResponseDto saveBoard(BoardModifyDto boardModifyDto){
        Member findMember = memberRepository.findById(boardModifyDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = Board.builder().title(boardModifyDto.getTitle()).content(boardModifyDto.getContent())
                .member(findMember).build();

        Board saveBoard = boardRepository.save(board);
        return new BoardResponseDto(saveBoard);
    }

    /**
     * 게시글 수정
     * */
    public BoardResponseDto updateBoard(Long boardId,BoardModifyDto boardModifyDto){
        Board toUpdateBoard = boardRepository.findById(boardId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Member boardMember = toUpdateBoard.getMember();
        if(boardMember.getId() != boardModifyDto.getMemberId()){
            throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
        }

        toUpdateBoard.updateBoard(boardModifyDto);

        return new BoardResponseDto(toUpdateBoard);
    }


    /**
     * 게시글 삭제
     * */
    public Long deleteBoard(Long boardId,BoardModifyDto boardModifyDto){
        Board toDeleteBoard = boardRepository.findById(boardId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Long memberId = toDeleteBoard.getMember().getId();
        if(memberId != boardModifyDto.getMemberId()){
            throw new IllegalArgumentException("게시글 작성자만 삭제할 수 있습니다.");
        }

        Long id = toDeleteBoard.getId();
        boardRepository.delete(toDeleteBoard);

        return id;
    }

    /**
     * 게시글 목록
     * */
    public Page<BoardResponseDto> getBoards( Pageable pageable){
        //TODO 나중에 queryDsl 을 이용해서 join fetch로 변경
        Page<Board> boards = boardRepository.findAllBy(pageable);

        return boards.map(BoardResponseDto::new);
    }

    /**
     * 게시글 상세보기
     * */
    public BoardResponseDto getBoardInfo(Long boardId,Long memberId){
        Board board = boardRepository.findById(boardId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boardShowView.showBoard(board,memberId);
        List<CommentResultDto> comments = getComments(boardId);
        BoardResponseDto boardResponseDto = new BoardResponseDto(board);
        boardResponseDto.addComment(comments);
        return boardResponseDto;
    }

    /**
     * 댓글 달기
     * */
    public void saveComment(CommentModifyDto commentModifyDto){
        Board board = boardRepository.findById(commentModifyDto.getBoardId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(commentModifyDto.getMemberId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        BoardComment comment = BoardComment.builder().member(member)
                .content(commentModifyDto.getContent()).board(board).build();
        if(commentModifyDto.getParentId() != null){
            boardCommentRepository.findById(commentModifyDto.getParentId()).ifPresent(comment::setParent);
        }

        board.addComment(comment);
        boardCommentRepository.save(comment);

    }

    /**
     * 댓글 수정
     * */
    public void updateComment(Long commentId,CommentModifyDto commentModifyDto){
        Board board = boardRepository.findById(commentModifyDto.getBoardId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(commentModifyDto.getMemberId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if(member.getId() != commentModifyDto.getMemberId()){
            throw new IllegalArgumentException("댓글 작성자만 수정할 수 있습니다.");
        }

        BoardComment comment = boardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.modifyContent(commentModifyDto.getContent(),null);

    }

    /**
     * 댓글 삭제
     * */
    public void deleteComment(Long commentId,CommentModifyDto commentModifyDto){
        Board board = boardRepository.findById(commentModifyDto.getBoardId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
        Member member = memberRepository.findById(commentModifyDto.getMemberId()).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if(member.getId() != commentModifyDto.getMemberId()){
            throw new IllegalArgumentException("댓글 작성자만 삭제할 수 있습니다.");
        }

        BoardComment comment = boardCommentRepository.findById(commentId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.modifyContent("삭제된 댓글입니다.", CommentStatus.DELETED);
    }

    /**
     * 입력,수정,삭제 후에 댓글 목록 조회 (이 메소드는 트랜잭션에 묶이면 안된다)
     * */
    @Transactional(readOnly = true)
    public List<CommentResultDto> getComments(Long boardId){
        List<BoardComment> boardComments = boardCommentRepository.findAllByBoardIdAndParentIsNull(boardId);
        return boardComments.stream().map(CommentResultDto::of).collect(Collectors.toList());
    }


}
