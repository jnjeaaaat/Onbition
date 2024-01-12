package org.jnjeaaaat.onbition.domain.dto.pay;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 잔액 입출금 Request class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeMoneyRequest {

  private BalanceType balanceType;

  @Min(1000)
  private Long changeBalance;

}
