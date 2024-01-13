package org.jnjeaaaat.onbition.service.impl.pay;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.ALREADY_OWNED_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.AMOUNT_LESS_THAN_CURRENT;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_PAINTING;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_FOUND_USER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NOT_SALE_PAINTING;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.domain.dto.paint.SimplePaintingDto;
import org.jnjeaaaat.onbition.domain.dto.pay.BalanceType;
import org.jnjeaaaat.onbition.domain.dto.pay.BuyPaintingResponse;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyRequest;
import org.jnjeaaaat.onbition.domain.dto.pay.ChangeMoneyResponse;
import org.jnjeaaaat.onbition.domain.dto.user.SimpleUserDto;
import org.jnjeaaaat.onbition.domain.entity.ElasticSearchPainting;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.pay.OwnerHistory;
import org.jnjeaaaat.onbition.domain.entity.pay.Payment;
import org.jnjeaaaat.onbition.domain.entity.pay.UserBalance;
import org.jnjeaaaat.onbition.domain.repository.ElasticSearchPaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.PaintingRepository;
import org.jnjeaaaat.onbition.domain.repository.UserRepository;
import org.jnjeaaaat.onbition.domain.repository.pay.OwnerHistoryRepository;
import org.jnjeaaaat.onbition.domain.repository.pay.PaymentRepository;
import org.jnjeaaaat.onbition.domain.repository.pay.UserBalanceRepository;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.BalanceService;
import org.jnjeaaaat.onbition.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 그림 구매, 결제 히스토리 service 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

  private final PaymentRepository paymentRepository;
  private final OwnerHistoryRepository ownerHistoryRepository;
  private final UserRepository userRepository;
  private final UserBalanceRepository userBalanceRepository;
  private final PaintingRepository paintingRepository;
  private final ElasticSearchPaintingRepository elasticSearchPaintingRepository;

  private final BalanceService balanceService;

  private final String ARTIST = "ROLE_ARTIST";

  /*
  [그림 구매]
  Request: user id, painting PK
  Response: simple details of Painting, buyer's current balance
   */
  @Override
  @Transactional
  public BuyPaintingResponse buyPainting(String uid, Long paintingId) {

    log.info("[buyPainting] 그림 구매 - 유저 id : {}, 그림 PK : {}", uid, paintingId);

    // 구매를 하는 유저
    User user = userRepository.findByUidAndDeletedAtNull(uid)
        .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

    // 구매하려는 그림
    Painting painting = paintingRepository.findById(paintingId)
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));


    // 구매하려는 유저의 현재 잔액
    Long currentBalance = userBalanceRepository.findAllByUserOrderByIdDesc(user)
        .stream().findFirst()
        .map(UserBalance::getCurrentBalance)
        .orElse(0L);

    // validation 확인
    validateBuyPainting(painting, user, currentBalance);

    /*
     결제 진행
     */
    paymentRepository.save(
        Payment.builder()
            .user(user)
            .painting(painting)
            .payAmount(painting.getPrice())
            .build()
    );

    // 결제하는 사람 잔액 출금
    ChangeMoneyRequest withdrawRequest =
        ChangeMoneyRequest.builder()
            .balanceType(BalanceType.WITHDRAW)
            .changeBalance(painting.getPrice())
            .build();
    // 현재 잔액을 포함한 response
    ChangeMoneyResponse withdrawResponse = balanceService.depositWithdraw(user.getUid(), withdrawRequest);

    // 그림 소유주한테 입금
    ChangeMoneyRequest depositRequest =
        ChangeMoneyRequest.builder()
            .balanceType(BalanceType.DEPOSIT)
            .changeBalance(painting.getPrice())
            .build();
    balanceService.depositWithdraw(painting.getUser().getUid(), depositRequest);

    // ARTIST 권한 추가
    if (painting.getUser().getRoles().contains("ROLE_DREAMER")) {  // 그림을 하나라도 등록한 유저일때
      painting.getUser().addRoles(ARTIST);
    }

    // 소유권 변경 & isSale false & 가격변경
    changeOwner(user, painting);

    return BuyPaintingResponse.builder()
        .painting(SimplePaintingDto.from(painting))
        .currentBalance(withdrawResponse.getCurrentBalance())  // 구매한 유저의 현재 잔액
        .build();
  }

  // 그림 구매 validation
  private void validateBuyPainting(Painting painting, User user, Long currentBalance) {
    // 본인 그림일때
    if (painting.getUser().equals(user)) {
      throw new BaseException(ALREADY_OWNED_PAINTING);
    }
    // 그림이 판매중이 아닐때
    if (painting.getIsSale().equals(false)) {
      throw new BaseException(NOT_SALE_PAINTING);
    }
    // 그림의 가격보다 잔액이 적을때
    if (painting.getPrice() > currentBalance) {
      throw new BaseException(AMOUNT_LESS_THAN_CURRENT);
    }
  }

  // 소유권 변경
  private void changeOwner(User user, Painting painting) {
    // ES
    ElasticSearchPainting esPainting = elasticSearchPaintingRepository.findById(painting.getId())
        .orElseThrow(() -> new BaseException(NOT_FOUND_PAINTING));

    painting.setUser(user);  // user 변경
    painting.setIsSale(false);  // 판매여부 false
    painting.setPrice(0L);   // 가격 0원으로 변경

    esPainting.setUser(SimpleUserDto.from(user));  // es user 변경
    esPainting.setIsSale(false);  // es 판매여부 false
    esPainting.setPrice(0L);   // 가격 0원으로 변경
    elasticSearchPaintingRepository.save(esPainting); // es 저장

    ownerHistoryRepository.save(
        OwnerHistory.builder()
            .user(user)
            .painting(painting)
            .build()
    );
  }

}
