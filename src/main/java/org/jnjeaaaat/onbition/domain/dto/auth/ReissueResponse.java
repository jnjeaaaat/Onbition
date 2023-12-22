package org.jnjeaaaat.onbition.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jnjeaaaat.onbition.domain.entity.token.Token;

/**
 * 토큰 재발급 Response Class
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReissueResponse {

  private String accessToken;

  public static ReissueResponse from(Token token) {
    return ReissueResponse.builder()
        .accessToken(token.getAccessToken())
        .build();
  }

}
