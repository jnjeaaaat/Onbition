package org.jnjeaaaat.onbition.service.impl.pay;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.AMOUNT_LESS_THAN_CURRENT;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.pay.BalanceType;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyRequest;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyResponse;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.pay.UserBalance;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.domain.repository.pay.UserBalanceRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.BalanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 입출금 service interface 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

  private final UserBalanceRepository userBalanceRepository;
  private final UserRepository userRepository;

  private final String BUYER = "ROLE_BUYER";

  /*
  [입출금]
  Request: user id, Change Money Type(deposit, withdraw), money amount
  Response: User details, Change Money Type(deposit, withdraw), money amount, current money
   */
  @Override
  @Transactional
  public ChangeMoneyResponse depositWithdraw(String uid, ChangeMoneyRequest request) {
    log.info("[depositWithdraw] 계좌 입금 & 출금 - 유저 id : {}", uid);

    User user = userRepository.findByUidAndDeletedAtNull(uid)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    Long currentBalance = userBalanceRepository.findAllByUserOrderByIdDesc(user)
        .stream().findFirst()
        .map(UserBalance::getCurrentBalance)
        .orElse(0L);

    UserBalance userBalance =
        UserBalance.builder()
            .user(user)
            .balanceType(request.getBalanceType())
            .changeBalance(request.getChangeBalance())
            .currentBalance(currentBalance)
            .build();

    // 입금
    if (request.getBalanceType().equals(BalanceType.DEPOSIT)) {
      log.info("[depositWithdraw] 입금 요청");
      depositMoney(user, userBalance, currentBalance, request.getChangeBalance());
    }
    // 출금
    else if (request.getBalanceType().equals(BalanceType.WITHDRAW)) {
      log.info("[depositWithdraw] 출금 요청");
      withdrawMoney(user, userBalance, currentBalance, request.getChangeBalance());
    }

    return ChangeMoneyResponse.from(userBalance);
  }

  // 입금
  private void depositMoney(User user, UserBalance userBalance, Long currentBalance,
      Long changeBalance) {
    userBalance.setCurrentBalance(currentBalance + changeBalance);

    // 잔액이 1000원 이상이면 BUYER 권한
    if (userBalance.getCurrentBalance() >= 1000) {
      user.addRoles(BUYER);
    }

    userBalanceRepository.save(userBalance);
  }

  // 출금
  private void withdrawMoney(User user, UserBalance userBalance, Long currentBalance,
      Long changeBalance) {
    // 잔액 부족
    if (changeBalance > currentBalance) {
      log.error("[withdrawMoney] 잔액 부족");
      throw new BaseException(AMOUNT_LESS_THAN_CURRENT);
    }
    userBalance.setCurrentBalance(currentBalance - changeBalance);

    // 잔액이 1000원 미만이 되면 BUYER 권한 없어짐
    if (userBalance.getCurrentBalance() < 1000) {
      user.removeRole(BUYER);
    }

    userBalanceRepository.save(userBalance);

  }
}
