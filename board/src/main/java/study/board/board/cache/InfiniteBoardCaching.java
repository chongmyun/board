package study.board.board.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import study.board.board.BoardJpaRepository;
import study.board.board.dto.BoardInfoDto;
import study.board.entity.Board;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static study.board.board.cache.OnceBoardCacing.*;

@Component
@Primary
@RequiredArgsConstructor
public class InfiniteBoardCaching implements BoardCaching {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  private final String BOARD_DETAIL = "board:%d:detail";

  private final RedisTemplate<String,Object> redisTemplate;

  private final BoardJpaRepository boardRepository;

  @Override
  public BoardInfoDto showBoard(Long boardId, Long memberId) {

    //1. 해당 게시글 상세정보 캐싱여부 조회
    HashOperations<String, Object, Object> boardDetailOp = redisTemplate.opsForHash();
    Long size = boardDetailOp.size(String.format(BOARD_DETAIL, boardId));

    if(size == 0){
      cachingBoard(boardId, boardDetailOp);
    }

    boardDetailOp.increment(String.format(BOARD_DETAIL, boardId),"viewCount",1);
    return getBoardInfoDto(boardId, boardDetailOp, BOARD_DETAIL, dateTimeFormatter);
  }

  @Override
  public Integer updateViewCount(Long boardId) {
    HashOperations<String, Object, Object> boardDetailOp = redisTemplate.opsForHash();
    Object o = boardDetailOp.get(String.format(BOARD_DETAIL, boardId), "viewCount");
    if(o != null){
      return Integer.parseInt(o.toString());
    }
    return null;
  }

  private void cachingBoard(Long boardId, HashOperations<String, Object, Object> boardDetailOp) {
    Board board = boardRepository.findById(boardId).
            orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    Map<String,Object> boardMap = new HashMap<>();
    boardMap.put("boardId",String.valueOf(board.getId()));
    boardMap.put("memberId",String.valueOf(board.getMember().getId()));
    boardMap.put("userId",board.getMember().getUserId());
    boardMap.put("title",board.getTitle());
    boardMap.put("content",board.getContent());
    boardMap.put("createdDate",board.getCreatedDate().format(dateTimeFormatter));
    boardMap.put("viewCount",String.valueOf(board.getViewCount()));
    boardDetailOp.putAll(String.format(BOARD_DETAIL, board.getId()),boardMap);
    redisTemplate.expire(String.format(BOARD_DETAIL, board.getId()),60*60,java.util.concurrent.TimeUnit.SECONDS);
  }
}
