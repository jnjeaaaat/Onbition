package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyRequest;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyResponse;

/**
 * 입출금 service interface
 */
public interface BalanceService {

  // 입출금
  ChangeMoneyResponse depositWithdraw(String uid, ChangeMoneyRequest request);
}
