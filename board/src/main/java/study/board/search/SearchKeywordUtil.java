package study.board.search;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;

@Component
public class SearchKeywordUtil {

  public static HashMap<String, TreeSet<String>> keywordMap = new HashMap<>();

  @PostConstruct
  public void init(){
    readProperties();
  }

  public String replaceKeyword(String content){
    String[] wordToken = content.split(" ");

    StringBuffer sb = new StringBuffer();

    boolean isReplace = false;
    for(String word : wordToken){
      for(String key : keywordMap.keySet()){
        if(keywordMap.get(key).contains(word)){
          sb.append(key);
          isReplace = true;
          break;
        }
      }
      if(!isReplace){
        sb.append(word);
      }
      sb.append(" ");
      isReplace = false;
    }
    return sb.toString();
  }



  private static void readProperties(){
    try {
      Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("wordChange.properties"));
      for(String key : properties.stringPropertyNames()){
        String[] values = properties.getProperty(key).split(",");
        TreeSet<String> valueSet = new TreeSet<>();
        for(String value : values){
          valueSet.add(value);
        }
        keywordMap.put(key, valueSet);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
