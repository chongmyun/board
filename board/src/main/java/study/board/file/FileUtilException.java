package study.board.file;

public class FileUtilException extends RuntimeException{
  public FileUtilException(String message) {
    super(message);
  }

  public FileUtilException(String message, Throwable cause) {
    super(message, cause);
  }
}
