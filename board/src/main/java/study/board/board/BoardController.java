package study.board.board;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import study.board.board.dto.BoardModifyDto;
import study.board.board.dto.BoardResponseDto;
import study.board.board.dto.CommentModifyDto;
import study.board.board.dto.CommentResultDto;

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
    public ResponseEntity<BoardResponseDto> saveBoard(@RequestBody BoardModifyDto boardModifyDto){
        BoardResponseDto boardResponseDto = boardService.saveBoard(boardModifyDto);
        return ResponseEntity.ok(boardResponseDto);
    }

    /**
     * 게시글 수정
     * */
    @PutMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable(name="id") Long boardId, @RequestBody BoardModifyDto boardModifyDto){
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardId,boardModifyDto);
        return ResponseEntity.ok(boardResponseDto);
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
    public ResponseEntity<Page<BoardResponseDto>> getBoards(@PageableDefault(direction = Sort.Direction.DESC,sort="createdDate") Pageable pageable){
        return ResponseEntity.ok(boardService.getBoards(pageable));
    }

    /**
     * 게시글 상세보기
     * */
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> getBoardInfo(@PathVariable Long id,@RequestParam Long memberId){
        return ResponseEntity.ok(boardService.getBoardInfo(id,memberId));
    }

    /**
     * 댓글 달기
     * */
    @PostMapping("/comment")
    public ResponseEntity<List<CommentResultDto>> saveComment(@RequestBody CommentModifyDto commentModifyDto){
        boardService.saveComment(commentModifyDto);
        List<CommentResultDto> commentResultDtos = boardService.getComments(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentResultDtos);
    }

    /**
     * 댓글 수정
     * */
    @PutMapping("/comment/{id}")
    public ResponseEntity<List<CommentResultDto>> updateComment(@PathVariable(name="id") Long commentId, @RequestBody CommentModifyDto commentModifyDto){
        boardService.updateComment(commentId,commentModifyDto);
        List<CommentResultDto> commentResultDtos = boardService.getComments(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentResultDtos);
    }

    /**
     * 댓글 삭제
     * */
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<List<CommentResultDto>> deleteComment(@PathVariable(name="id") Long commentId, @RequestBody CommentModifyDto commentModifyDto){
        boardService.deleteComment(commentId,commentModifyDto);
        List<CommentResultDto> commentResultDtos = boardService.getComments(commentModifyDto.getBoardId());
        return ResponseEntity.ok(commentResultDtos);
    }


}
