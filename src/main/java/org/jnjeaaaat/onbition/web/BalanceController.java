package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_CHANGE_MONEY;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.annotation.AuthUser;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyRequest;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyResponse;
import org.jnjeaaaat.onbition.service.BalanceService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 입출금 api
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/balances")
public class BalanceController {

  private final BalanceService balanceService;

  /*
  [입출금]
  Request: user id, Change Money Type(deposit, withdraw), money amount
  Response: User details, Change Money Type(deposit, withdraw), money amount, current money
   */
  @PostMapping
  public BaseResponse<ChangeMoneyResponse> depositWithdraw(
      @AuthUser UserDetails userDetails,
      @Valid @RequestBody ChangeMoneyRequest request
  ) {

    log.info("[depositWithdraw] 계좌 입금 & 출금 요청");

    return BaseResponse.success(
        SUCCESS_CHANGE_MONEY,
        balanceService.depositWithdraw(userDetails.getUsername(), request)
    );

  }

}
