package study.board.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class LocalFileUtil implements FileUtil{

  @Value("${file.root.path}")
  private String rootPath;

  @Value("${file.upload.path}")
  private String uploadPath;

  @Value("${file.store.path}")
  private String storePath;

  @Override
  public String fileUpload(MultipartFile file) {
    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
    Path path = Paths.get(rootPath+uploadPath,fileName);
    Path parentDir = path.getParent();
    try {

      if(!Files.exists(parentDir)){
        Files.createDirectories(parentDir);
      }

      Files.write(path, file.getBytes());
    } catch (IOException e) {
      throw new FileUtilException("파일을 업로드할 수 없습니다.",e);
    }
    return fileName;
  }
  @Override
  public Resource fileDownload(String fileName) throws FileNotFoundException {
    try {
      Path path = Paths.get(rootPath+uploadPath,fileName);
      Path parentDir = path.getParent();
      if(!Files.exists(parentDir)){
          Files.createDirectories(parentDir);
      }

      Resource resource = new PathResource(path);
      if(!resource.exists()){
        throw new FileNotFoundException("파일을 찾을 수 없습니다.");
      }
      return resource;
    }catch (IOException e) {
      throw new FileUtilException("폴더를 생성할 수 없습니다.",e);
    }
  }

  @Override
  public FileInfoDto moveTempFile(String fileName,Long id) {

    String originalFileName = fileName.substring(fileName.indexOf("_") + 1);
    String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    String storedFilePath = storePath+id;

    FileInfoDto fileInfo = FileInfoDto.builder().storedFileName(fileName)
            .storedFilePath(storedFilePath).originalFileName(originalFileName).rootPath(rootPath).extension(extension).build();
    try {
      Path source = Paths.get(rootPath+uploadPath,fileName);
      Path target = Paths.get(rootPath+storedFilePath,fileName);

      Path parent = target.getParent();
      if(!Files.exists(parent)){
        Files.createDirectories(parent);
      }
//      Files.move(source, target);
      return fileInfo;
    } catch (IOException e) {
      throw new FileUtilException("파일 이동 중 오류가 발생했습니다.",e);
    }
  }
}
