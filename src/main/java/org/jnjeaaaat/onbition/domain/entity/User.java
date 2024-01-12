package org.jnjeaaaat.onbition.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jnjeaaaat.onbition.domain.entity.pay.UserBalance;

/**
 * SpringSecurity UserDetails 구현하는 User Entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // 유저 PK

  @Column(nullable = false)
  private String uid; // 유저 id

  @JsonProperty(access = Access.WRITE_ONLY)
  @Column(nullable = false)
  private String password; // 유저 비밀번호

  @Column(nullable = false)
  private String profileImgUrl; // 유저 프로필사진 url

  @Column(nullable = false)
  private String name;  // 유저 이름

  @Column(nullable = false)
  private String phone; // 유저 핸드폰번호

  @Column
  private String cardNum; // 유저 카드번호

  @Column
  private String accountNum; // 유저 계좌번호

  @Column
  private LocalDateTime deletedAt;  // 유저 삭제 일자

  @JdbcTypeCode(SqlTypes.JSON) // JSON 타입으로 저장
  @Column(nullable = false)
  @Builder.Default
  private Set<String> roles = new HashSet<>(); // 유저 권한 리스트

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Painting> paintings = new ArrayList<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<UserBalance> balances = new ArrayList<>();

  /*
  유저 새로운 권한 추가 method
   */
  public boolean addRoles(String newRole) {
    return this.roles.add(newRole);
  }

  /*
  유저 권한 제거 method
   */
  public boolean removeRole(String role) {
    return this.roles.remove(role);
  }
}
