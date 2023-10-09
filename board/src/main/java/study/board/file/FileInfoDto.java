package study.board.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileInfoDto {

  private String rootPath;
  private String storedFilePath;
  private String originalFileName;
  private String storedFileName;
  private String extension;

  public FileInfoDto() {
  }

  @Builder
  public FileInfoDto(String rootPath, String storedFilePath, String originalFileName, String storedFileName, String extension) {
    this.rootPath = rootPath;
    this.storedFilePath = storedFilePath;
    this.originalFileName = originalFileName;
    this.storedFileName = storedFileName;
    this.extension = extension;
  }
}
