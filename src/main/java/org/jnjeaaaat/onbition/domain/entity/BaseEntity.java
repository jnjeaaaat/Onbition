package org.jnjeaaaat.onbition.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder(builderMethodName = "doesNotUseThisBuilder")
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  // 생성일자
  @CreatedDate
  private LocalDateTime createdAt;

  // 정보 변경 일자
  @LastModifiedDate
  private LocalDateTime updatedAt;

}
