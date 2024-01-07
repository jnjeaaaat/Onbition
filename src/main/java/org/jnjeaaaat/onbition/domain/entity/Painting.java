package org.jnjeaaaat.onbition.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Painting Entity Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
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

  @JdbcTypeCode(SqlTypes.JSON)   // JSON 타입으로 지정
  @Column(nullable = false)
  @Builder.Default
  private Set<String> tags = new HashSet<>();    // tags 리스트

}
