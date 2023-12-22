package org.jnjeaaaat.onbition.service.impl.auth;

import lombok.RequiredArgsConstructor;
import org.jnjeaaaat.onbition.domain.entity.token.Token;
import org.jnjeaaaat.onbition.domain.repository.TokenRepository;
import org.jnjeaaaat.onbition.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Token 저장, 삭제 기능 Interface 구현체 Class
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

  private final TokenRepository tokenRepository;

  @Override
  @Transactional
  public void saveTokenInfo(Token token) {
    tokenRepository.save(token);
  }

  @Override
  @Transactional
  public void removeRefreshToken(String token) {
    tokenRepository.findByRefreshToken(token)
        .ifPresent(tokenRepository::delete);
  }
}
