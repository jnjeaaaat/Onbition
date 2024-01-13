package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.dto.pay.BuyPaintingResponse;

/**
 * 그림 구매, 결제 히스토리 service
 */
public interface PaymentService {

  // 그림 구매
  BuyPaintingResponse buyPainting(String uid, Long paintingId);
}
