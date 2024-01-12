package org.jnjeaaaat.onbition.domain.entity.pay;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Enumerated;
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
import org.jnjeaaaat.onbition.domain.dto.pay.BalanceType;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 유저 잔액 entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class UserBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // user balance pk

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user; // 해당 유저

  @Enumerated
  @Column(nullable = false)
  private BalanceType balanceType;  // 입출금 타입

  @Column(nullable = false)
  private Long changeBalance; // 변화된 잔액

  @Column(nullable = false)
  private Long currentBalance;  // 현재 잔액

  @CreatedDate
  private LocalDateTime createdAt;  // 입출금 일자

}
