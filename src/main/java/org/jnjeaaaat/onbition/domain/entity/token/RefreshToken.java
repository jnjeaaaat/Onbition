package org.jnjeaaaat.onbition.domain.entity.token;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Redis 에서 관리할 RefreshToken Entity Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refToken")
public class RefreshToken implements Serializable {

  @Id
  @Indexed
  private String uid;   // 유저 아이디

  @Indexed
  private String token;   // refreshToken

  @TimeToLive
  private Long expiredTime;   // 만료기간

}