package study.board.board.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.board.board.BoardJpaRepository;
import study.board.board.dto.BoardInfoDto;
import study.board.entity.Board;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Transactional
public class OnceBoardCacing implements BoardCaching {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private final String BOARD_DETAIL = "board:%d:detail";
  private final String USER_BOARD_LIST = "user:%d:board:list";

  private final RedisTemplate<String,Object> redisTemplate;

  private final BoardJpaRepository boardRepository;
  @Override
  public BoardInfoDto showBoard(Long boardId, Long memberId) {

    HashOperations<String, Object, Object> boardDetailOp = redisTemplate.opsForHash();

    SetOperations<String, Object> setOp = redisTemplate.opsForSet();
    Long size = setOp.size(String.format(USER_BOARD_LIST, memberId));

    if(size == 0){
      cachingBoard(boardId,boardDetailOp);
    }

    Boolean isShow = setOp.isMember(String.format(USER_BOARD_LIST, memberId), boardId);
    if(Boolean.FALSE.equals(isShow)){
      boardDetailOp.increment(String.format(BOARD_DETAIL, boardId),"viewCount",1);
    }
    return getBoardInfoDto(boardId, boardDetailOp, BOARD_DETAIL, dateTimeFormatter);
  }

  @Override
  public Integer updateViewCount(Long boardId) {
    HashOperations<String, Object, Object> boardDetailOp = redisTemplate.opsForHash();
    Object o = boardDetailOp.get(String.format(BOARD_DETAIL, boardId), "viewCount");
    redisTemplate.delete(String.format(BOARD_DETAIL, boardId));
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

  static BoardInfoDto getBoardInfoDto(Long boardId, HashOperations<String, Object, Object> boardDetailOp, String boardDetail, DateTimeFormatter dateTimeFormatter) {
    Map<Object, Object> entries = boardDetailOp.entries(String.format(boardDetail, boardId));
    return BoardInfoDto.builder()
            .boardId(Long.parseLong(entries.get("boardId").toString()))
            .memberId(Long.parseLong(entries.get("memberId").toString()))
            .userId(entries.get("userId").toString())
            .title(entries.get("title").toString())
            .content(entries.get("content").toString())
            .createdDate(LocalDateTime.parse(entries.get("createdDate").toString(), dateTimeFormatter))
            .viewCount(Integer.parseInt(entries.get("viewCount").toString()))
            .build();
  }

}
