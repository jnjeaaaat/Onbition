package org.jnjeaaaat.onbition.util;

import javax.annotation.PostConstruct;
import lombok.Data;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 문자전송 Util Class
 */
@Component
@ConfigurationProperties(prefix = "coolsms.api")
@Data
public class SmsUtil {

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

  // 단일 메시지 발송 예제
  public SingleMessageSentResponse sendOne(String to, String verificationCode) {
    Message message = new Message();
    // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
    message.setFrom(from);
    message.setTo(to.replace("-", ""));
    message.setText("[Onbition] 아래의 인증번호를 입력해주세요\n" + verificationCode);

    return this.defaultMessageService.sendOne(new SingleMessageSendingRequest(message));
  }


}
