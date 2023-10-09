package study.board.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.board.config.SftpConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static study.board.config.SftpConfig.*;

@SpringBootTest
public class Testtest {

  @Autowired
  private UploadGateway uploadGateway;

  @Test
  public void testUploan() throws IOException{
    Path tempFile = Files.createTempFile("UPLOAD_TEST",".txt");

    uploadGateway.upload(tempFile.toFile());


  }
}
