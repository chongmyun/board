package study.board.file;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileUtil {

  String fileUpload(MultipartFile file) throws IOException;

  Resource fileDownload(String fileName) throws FileNotFoundException;

  FileInfoDto moveTempFile(String fileName, Long id);

}
