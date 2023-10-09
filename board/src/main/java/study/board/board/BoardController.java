package study.board.board;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.board.board.cond.BoardSearchCondition;
import study.board.board.dto.BoardInfoDto;
import study.board.board.dto.BoardModifyDto;
import study.board.board.dto.CommentModifyDto;
import study.board.board.dto.CommentListDto;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @ExceptionHandler
    public ResponseEntity<String> handle(IllegalArgumentException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * 게시글 저장
     * */
    @PostMapping("/board/save")
    public ResponseEntity<BoardInfoDto> saveBoard(@RequestBody BoardModifyDto boardModifyDto){
        BoardInfoDto boardInfoDto = boardService.saveBoard(boardModifyDto);

        return ResponseEntity.ok(boardInfoDto);
    }

    /**
     * 게시글 수정
     * */
    @PutMapping("/board/{id}")
    public ResponseEntity<BoardInfoDto> updateBoard(@PathVariable(name="id") Long boardId, @RequestBody BoardModifyDto boardModifyDto){
        BoardInfoDto boardInfoDto = boardService.updateBoard(boardId,boardModifyDto);
        return ResponseEntity.ok(boardInfoDto);
    }

    /**
     * 게시글 삭제
     * */
    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable(name="id") Long boardId, @RequestBody BoardModifyDto boardModifyDto){
        boardService.deleteBoard(boardId,boardModifyDto);
        return ResponseEntity.ok("게시글을 삭제했습니다.");
    }

    /**
     * 게시글 목록
     * */
    @GetMapping("/boards")
    public ResponseEntity<Page<BoardInfoDto>> getBoards(@PageableDefault(direction = Sort.Direction.DESC,sort="createdDate") Pageable pageable,
                                                        BoardSearchCondition condition){
        return ResponseEntity.ok(boardService.getBoards(pageable,condition));
    }

    /**
     * 게시글 상세보기
     * */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardInfoDto> getBoardInfo(@PathVariable Long id, @RequestParam Long memberId){
        return ResponseEntity.ok(boardService.getBoardInfo(id,memberId));
    }

    /**
     * 댓글 달기
     * */
    @PostMapping("/comment")
    public ResponseEntity<List<CommentListDto>> saveComment(@RequestBody CommentModifyDto commentModifyDto){
        boardService.saveComment(commentModifyDto);
        List<CommentListDto> commentListDtos = boardService.getCommentsAdvance(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentListDtos);
    }

    /**
     * 댓글 수정
     * */
    @PutMapping("/comment/{id}")
    public ResponseEntity<List<CommentListDto>> updateComment(@PathVariable(name="id") Long commentId, @RequestBody CommentModifyDto commentModifyDto){
        boardService.updateComment(commentId,commentModifyDto);
        List<CommentListDto> commentListDtos = boardService.getComments(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentListDtos);
    }

    /**
     * 댓글 삭제
     * */
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<List<CommentListDto>> deleteComment(@PathVariable(name="id") Long commentId, @RequestBody CommentModifyDto commentModifyDto){
        boardService.deleteComment(commentId,commentModifyDto);
        List<CommentListDto> commentListDtos = boardService.getComments(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentListDtos);
    }


}
