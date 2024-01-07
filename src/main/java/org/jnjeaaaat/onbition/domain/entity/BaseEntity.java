package org.jnjeaaaat.onbition.domain.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity 들에 공통적으로 사용되는
 * 생성일자, 정보 변경일자
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

  // 생성일자
  @CreatedDate
  private LocalDateTime createdAt;

  // 정보 변경 일자
  @LastModifiedDate
  private LocalDateTime updatedAt;

}
