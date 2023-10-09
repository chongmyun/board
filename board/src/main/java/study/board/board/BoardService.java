package study.board.board;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.board.board.cond.BoardSearchCondition;
import study.board.board.dto.BoardInfoDto;
import study.board.board.dto.BoardModifyDto;
import study.board.board.dto.CommentModifyDto;
import study.board.board.dto.CommentListDto;
import study.board.board.cache.BoardCaching;
import study.board.entity.*;
import study.board.file.FileInfoDto;
import study.board.file.FileUtil;
import study.board.member.MemberJpaRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardJpaRepository boardRepository;

    private final BoardCommentJpaRepository boardCommentRepository;

    private final MemberJpaRepository memberRepository;

    private final BoardCaching boardCaching;

    private final FileUtil fileUtil;



    @PostConstruct
    public void init(){
        Member member = Member.builder().userId("chdaud33").password("1234").name("정총명").build();
        memberRepository.save(member);
        for(int i= 0 ; i < 100 ; i++){
            Board board = Board.builder().member(member).title("제목" + i).content("내용" + i).build();
            BoardDetail boardDetail = BoardDetail.builder().detailContent("내용 입력").build();
            board.addBoardDetail(boardDetail);
            boardRepository.save(board);
        }
    }

    /**
     * 게시글 저장
     * */
    public BoardInfoDto saveBoard(BoardModifyDto boardModifyDto){
        Member findMember = memberRepository.findById(boardModifyDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Board board = Board.builder().title(boardModifyDto.getTitle()).content(boardModifyDto.getContent())
                .member(findMember).build();

        Board saveBoard = boardRepository.save(board);

        String contentFileList = boardModifyDto.getContentFileList();
        if(StringUtils.hasText(contentFileList)){
            String[] fileNames = contentFileList.split(",");
            for(String fileName : fileNames){
                FileInfoDto fileInfoDto = fileUtil.moveTempFile(fileName, saveBoard.getId());
                BoardFiles boardFiles = new BoardFiles(fileInfoDto);
                saveBoard.addBoardFiles(boardFiles);
                boardRepository.saveBoardFiles(boardFiles);
            }
        }
        return new BoardInfoDto(saveBoard);
    }

    /**
     * 게시글 수정
     * */
    public BoardInfoDto updateBoard(Long boardId, BoardModifyDto boardModifyDto){
        Board toUpdateBoard = boardRepository.findById(boardId).
                orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Member boardMember = toUpdateBoard.getMember();
        if(boardMember.getId() != boardModifyDto.getMemberId()){
            throw new IllegalArgumentException("게시글 작성자만 수정할 수 있습니다.");
        }

        toUpdateBoard.updateBoard(boardModifyDto);
        Integer viewCount = boardCaching.updateViewCount(toUpdateBoard.getId());
        if(viewCount != null) toUpdateBoard.addViewCount(viewCount);

        return new BoardInfoDto(toUpdateBoard);
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

        boardRepository.softDeleteBoard(boardId);
        return boardId;
    }

    /**
     * 게시글 목록
     * */
    public Page<BoardInfoDto> getBoards(Pageable pageable, BoardSearchCondition condition){
        //TODO 나중에 queryDsl 을 이용해서 join fetch로 변경 -> dto로 변환
        return boardRepository.findBoardList(pageable,condition);
    }

    /**
     * 게시글 상세보기
     * */
    public BoardInfoDto getBoardInfo(Long boardId, Long memberId){

        BoardInfoDto boardInfoDto = boardCaching.showBoard(boardId, memberId);
        List<CommentListDto> comments = getCommentsAdvance(boardId);
        boardInfoDto.addComment(comments);
        return boardInfoDto;
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
    public List<CommentListDto> getComments(Long boardId){
        List<BoardComment> boardComments = boardCommentRepository.findAllByBoardIdAndParentIsNull(boardId);
        return boardComments.stream().map(CommentListDto::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CommentListDto> getCommentsAdvance(Long boardId){
        List<CommentListDto> commentList = boardRepository.findCommentList(boardId);
        List<CommentListDto> resultList = new ArrayList<>();

        HashMap<Long, List<CommentListDto>> collect = commentList.stream().collect(Collectors.toMap(
                CommentListDto::getParentId,
                x -> {
                    List<CommentListDto> subList = new ArrayList<>();
                    System.out.println(x);
                    subList.add(x);
                    return subList;
                },
                (left, right) -> {
                    System.out.println(right);
                    left.addAll(right);
                    return left;
                },
                HashMap::new
        ));

        for(Long key : collect.keySet()){
            List<CommentListDto> commentListDtos = collect.get(key);
            for(CommentListDto commentListDto : commentListDtos){
                List<CommentListDto> children = collect.get(commentListDto.getCommentId());
                if(children != null) commentListDto.getChild().addAll(children);

            }
            if(key == null) resultList.addAll(commentListDtos);
        }

        return resultList;

    }



}
