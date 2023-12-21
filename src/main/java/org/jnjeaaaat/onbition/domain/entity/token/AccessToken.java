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
 * Redis 에서 관리할 AccessToken Entity Class
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "accToken")
public class AccessToken implements Serializable {

  @Id
  @Indexed
  private String uid;  // 사용자 아이디

  @Indexed
  private String token;  // accessToken

  @TimeToLive
  private Long expiredTime;  // 만료기간

}