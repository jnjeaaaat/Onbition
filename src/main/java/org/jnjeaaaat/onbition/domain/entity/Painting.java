package org.jnjeaaaat.onbition.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

/**
 * Painting Entity Class
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
public class Painting extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;      // painting PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;    // 그림 소유자

  @Column(nullable = false)
  private String paintingImgUrl;    // 그림 url

  @Column(nullable = false)
  private String title;   // 그림 제목

  @Column(nullable = false)
  private String description;  // 그림 설명

  @Column(nullable = false)
  private boolean isSale;     // 그림 판매 여부

  @Column(nullable = false)
  private Long auctionPrice;    // 경매 시작 가격

  @Column(nullable = false)
  private Long salePrice;   // 매매 가격

  @Type(type = "json")
  @Column(columnDefinition = "json")
  @Builder.Default
  private Set<String> tags = new HashSet<>();    // tags 리스트

}
