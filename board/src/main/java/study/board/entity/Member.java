package study.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@Entity
@NoArgsConstructor
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String userId;

  private String password;

  private String name;

  @Builder
  public Member(String userId, String password, String name,@Nullable Long id){
    this.userId = userId;
    this.password = password;
    this.name = name;
    this.id = id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
