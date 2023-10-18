package study.board.search;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchKeywordUtilTest {

  @Autowired
  SearchKeywordUtil searchKeywordUtil;

  @Test
  public void test(){
    String content = "검정 리무진 바퀴";
    String word = searchKeywordUtil.replaceKeyword(content);
    System.out.println(word);
  }

}