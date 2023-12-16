package org.jnjeaaaat.onbition.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.vladmihalcea.hibernate.type.json.JsonType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * SpringSecurity UserDetails 구현하는 User Entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@TypeDef(name = "json", typeClass = JsonType.class)
/*
 type name(식별자라고 이해하면됨) 을 "json"으로 지정
 "json" -> JsonType.class 로 지정하라는 뜻.
 */
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

  @Type(type = "json") // TypeDef 를 통해 선언한 "json" Type 적용
  @Column(columnDefinition = "json") // MySQL column 도 "json" 적용
  @Builder.Default
  private List<String> roles = new ArrayList<>(); // 유저 권한 리스트

}
