package org.jnjeaaaat.onbition.domain.entity.pay;

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
 * 그림 소유권 변경 history entity
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class OwnerHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;    // history pk

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;  // 소유주

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "painting_id")
  private Painting painting;  // 그림

  @CreatedDate
  private LocalDateTime ownedAt;  // 소유된 일자

}
