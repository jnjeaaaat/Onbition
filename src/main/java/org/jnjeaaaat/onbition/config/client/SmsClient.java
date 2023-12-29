package org.jnjeaaaat.onbition.config.client;

import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 문자전송 Client Class
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "coolsms.api")
@Data
public class SmsClient {

  private String from;
  private String key;
  private String secret;

  private DefaultMessageService defaultMessageService;

  /*
  API key, secret key initializing
   */
  @PostConstruct
  private void init() {
    this.defaultMessageService = NurigoApp.INSTANCE.initialize(key, secret,
        "https://api.coolsms.co.kr");
  }

  // 회원가입 인증코드 발송
  public SingleMessageSentResponse sendVerificationCode(String to, String verificationCode) {

    log.info("[sendMessage] phone number : {}, code : {}", to, verificationCode);
    Message message = new Message();
    // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
    message.setFrom(from);
    message.setTo(to.replace("-", ""));
    message.setText("[Onbition] 아래의 인증번호를 입력해주세요.\n" + verificationCode);

    return this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
  }

  // 비밀번호 초기화 문자 발송
  public SingleMessageSentResponse sendPasswordResetCode(String to, String newPassword) {

    log.info("[sendMessage] phone number : {}, password : {}", to, newPassword);
    Message message = new Message();
    // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
    message.setFrom(from);
    message.setTo(to.replace("-", ""));
    message.setText("[Onbition] 비밀번호가 초기화 되었습니다.\n" + newPassword);

    return this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
  }


}
