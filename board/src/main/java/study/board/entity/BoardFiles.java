package study.board.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;
import study.board.file.FileInfoDto;

@Entity
@NoArgsConstructor
public class BoardFiles {
  @Id
  @GeneratedValue
  @Column(name = "file_id")
  private Long id;
  private String rootPath;
  private String storedFilePath;
  private String originalFileName;
  private String storedFileName;
  private String extension;

  @Builder
  public BoardFiles(FileInfoDto fileInfoDto) {
    this.rootPath = fileInfoDto.getRootPath();
    this.storedFilePath = fileInfoDto.getStoredFilePath();
    this.originalFileName = fileInfoDto.getOriginalFileName();
    this.storedFileName = fileInfoDto.getStoredFileName();
    this.extension = fileInfoDto.getExtension();
  }


}
