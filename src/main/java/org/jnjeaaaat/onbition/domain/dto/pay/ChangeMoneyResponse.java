package org.jnjeaaaat.onbition.domain.dto.pay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.dto.user.SimpleUserDto;
import org.jnjeaaaat.onbition.domain.entity.pay.UserBalance;

/**
 * 입출금 Response class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeMoneyResponse {

  private SimpleUserDto user;
  private BalanceType balanceType;
  private Long changeBalance;
  private Long currentBalance;

  public static ChangeMoneyResponse from(UserBalance userBalance) {
    return ChangeMoneyResponse.builder()
        .user(SimpleUserDto.from(userBalance.getUser()))
        .balanceType(userBalance.getBalanceType())
        .changeBalance(userBalance.getChangeBalance())
        .currentBalance(userBalance.getCurrentBalance())
        .build();

  }

}
