package org.jnjeaaaat.onbition.redis;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jnjeaaaat.onbition.config.redis.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisTest {

  @Autowired
  RedisService redisService;

  @Test
  @DisplayName("레디스 데이터 저장")
  void set_to_redis() {
    //given
    String phone = "010-1234-1234";
    String verificationCode = "000000";
    Long expired = 60 * 3L;

    //when
    redisService.setDataExpire(phone, verificationCode, expired);

    //then
    assertEquals(redisService.getData(phone), verificationCode);
  }

}
