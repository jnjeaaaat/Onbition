package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.entity.token.AccessToken;
import org.jnjeaaaat.onbition.domain.entity.token.RefreshToken;

/**
 * Token 저장, 삭제 기능 Interface
 */
public interface TokenService {

  // accessToken 정보 저장
  void saveTokenInfo(AccessToken token);

  // refreshToken 정보 저장
  void saveTokenInfo(RefreshToken token);

  // token 삭제
  void removeRefreshToken(String token);

}
