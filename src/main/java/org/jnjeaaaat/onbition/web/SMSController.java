package org.jnjeaaaat.onbition.web;

import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_AUTH_TEXT;
import static org.jnjeaaaat.onbition.domain.dto.base.BaseStatus.SUCCESS_SEND_TEXT;

import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jnjeaaaat.onbition.config.annotation.valid.Telephone;
import org.jnjeaaaat.onbition.domain.dto.auth.SendTextResponse;
import org.jnjeaaaat.onbition.domain.dto.base.BaseResponse;
import org.jnjeaaaat.onbition.service.SMSService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증코드 전송, 인증코드 확인 api
 */
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sms")
public class SMSController {

  private final SMSService smsService;

  // 문자로 인증코드 전송
  @PostMapping("")
  public BaseResponse<SendTextResponse> sendMessage(@RequestParam("phone") @Telephone String phone) {

    log.info("[inputPhoneNum] 핸드폰 번호 본인 인증");

    return BaseResponse.success(
        SUCCESS_SEND_TEXT,
        smsService.sendMessage(phone)
    );
  }

  // 인증코드 일치
  @PostMapping("/authentication")
  public BaseResponse<String> authenticatePhoneNum(
      @RequestParam("phone") @Telephone String phone,
      @RequestParam("code") @NotBlank String code) {

    log.info("[authenticatePhoneNum] 핸드폰 번호 본인 인증 - 코드 입력");

    return BaseResponse.success(
        SUCCESS_AUTH_TEXT,
        smsService.authenticatePhoneNum(phone, code)
    );

  }

}
