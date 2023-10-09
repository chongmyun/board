package study.board.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class FileController {

  private final FileUtil fileUtil;

  @PostMapping("/file/upload")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    String fileName = fileUtil.fileUpload(file);
    return ResponseEntity.ok(fileName);
  }

  @GetMapping("/file/download")
  public ResponseEntity<Resource> downloadFile(@RequestParam String fileName) throws IOException {

    CacheControl cacheControl = CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic();
    Resource resource = fileUtil.fileDownload(fileName);
    String eTag = UUID.randomUUID().toString();
    return ResponseEntity.ok()
            .eTag(eTag)
            .cacheControl(cacheControl)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
            .body(resource);
  }

}
