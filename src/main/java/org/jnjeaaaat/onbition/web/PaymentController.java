package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_BUY_PAINTING;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.annotation.AuthUser;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.domain.dto.pay.BuyPaintingResponse;
import org.jnjeaaaat.onbition.service.PaymentService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 그림 구매, 결제 히스토리 api
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

  private final PaymentService paymentService;

  /*
  [그림 구매]
  Request: user id, painting PK
  Response: simple details of Painting, buyer's current balance
   */
  @PostMapping("/{paintingId}")
  public BaseResponse<BuyPaintingResponse> buyPainting(
      @AuthUser UserDetails userDetails,
      @PathVariable Long paintingId) {

    log.info("[buyPainting] 그림 구매 요청");

    return BaseResponse.success(
        SUCCESS_BUY_PAINTING,
        paymentService.buyPainting(userDetails.getUsername(), paintingId)
    );
  }


}
