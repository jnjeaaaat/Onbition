package org.jnjeaaaat.onbition.service;

import org.jnjeaaaat.onbition.domain.entity.token.Token;

/**
 * Token 저장, 삭제 기능 Interface
 */
public interface TokenService {

  // Token 정보 저장
  void saveTokenInfo(Token token);

  // token 삭제
  void removeRefreshToken(String token);

}
