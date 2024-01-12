package org.jnjeaaaat.onbition.domain.entity.pay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 결제 현황 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // payment PK

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;    // 결제한 유저

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "painting_id")
  private Painting painting;    // 결제된 그림

  @Column(nullable = false)
  private Long payAmount; // 결제 금액

  @CreatedDate
  private LocalDateTime createdAt;  // 결제 일자

}
