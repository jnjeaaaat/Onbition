package org.jnjeaaaat.onbition.service.impl.auth;

import lombok.RequiredArgsConstructor;
import org.jnjeaaaat.onbition.domain.entity.token.AccessToken;
import org.jnjeaaaat.onbition.domain.entity.token.RefreshToken;
import org.jnjeaaaat.onbition.domain.repository.AccessTokenRepository;
import org.jnjeaaaat.onbition.domain.repository.RefreshTokenRepository;
import org.jnjeaaaat.onbition.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Token 저장, 삭제 기능 Interface 구현체 Class
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final AccessTokenRepository accessTokenRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  @Override
  @Transactional
  public void saveTokenInfo(AccessToken token) {
    accessTokenRepository.save(token);
  }

  @Override
  @Transactional
  public void saveTokenInfo(RefreshToken token) {
    refreshTokenRepository.save(token);
  }

  @Override
  @Transactional
  public void removeRefreshToken(String token) {
    refreshTokenRepository.findByToken(token)
        .ifPresent(refreshTokenRepository::delete);
  }
}
