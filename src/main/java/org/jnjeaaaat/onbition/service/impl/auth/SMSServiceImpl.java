package org.jnjeaaaat.onbition.service.impl.auth;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.NEED_REPOST_PHONE_NUMBER;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.UN_MATCH_VERIFICATION_CODE;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.jnjeaaaat.onbition.config.client.SmsClient;
import org.jnjeaaaat.onbition.domain.dto.auth.SendTextResponse;
import org.jnjeaaaat.onbition.exception.BaseException;
import org.jnjeaaaat.onbition.service.SMSService;
import org.jnjeaaaat.onbition.util.RedisUtil;
import org.springframework.stereotype.Service;

/**
 * 문자전송 서비스 SMSService interface 구현체 Class
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SMSServiceImpl implements SMSService {

  private final SmsClient smsClient;
  private final RedisUtil redisUtil;

  private final Long expireTimeMs = 60 * 3L; // 인증코드 유효기간

  /*
  [문자전송, Redis 저장]
  phone 번호를 입력받아 인증코드를 전송하고
  Redis server 에 {전화번호:인증코드} 값 저장
   */
  @Override
  public SendTextResponse sendMessage(String phone) {

    String verificationCode = getVerificationCode();

    log.info("[sendMessage] 해당 번호로 문자 전송");
    // 문자인증을 위한 문자 전송
    smsClient.sendVerificationCode(phone, verificationCode);
    log.info("[sendMessage] 문자 전송 성공");

    log.info("[saveToRedis] Redis 서버에 인증코드 저장");
    redisUtil.setDataExpire(phone, verificationCode, expireTimeMs);
    log.info("[saveToRedis] Redis 서버에 인증코드 저장 성공");

    return new SendTextResponse(verificationCode);
  }

  /*
  [인증코드 비교]
  phone 번호와 code 를 비교해서
  null 이 아니고 일치하면 phone 번호 다시 리턴
   */
  @Override
  public String authenticatePhoneNum(String phone, String code) {
    // key 값이 없을 때
    if (redisUtil.getData(phone) == null) {
      throw new BaseException(NEED_REPOST_PHONE_NUMBER);
    }
    // value 값과 code 값이 다를때
    if (!Objects.equals(redisUtil.getData(phone), code)) {
      throw new BaseException(UN_MATCH_VERIFICATION_CODE);
    }

    return phone;
  }

  /*
  6자리 랜덤숫자 String return
   */
  private String getVerificationCode() {
    return RandomStringUtils.randomNumeric(6);
  }
}
