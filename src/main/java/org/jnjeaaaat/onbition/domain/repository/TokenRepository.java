package org.jnjeaaaat.onbition.domain.repository;

import java.util.Optional;
import org.jnjeaaaat.onbition.domain.entity.token.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * AccessToken Repository
 */
@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

  // Redis 에서 AccessToken 값으로 Token 추출
  Optional<Token> findByAccessToken(String accessToken);

  // Redis 에서 RefreshToken 값으로 Token 추출
  Optional<Token> findByRefreshToken(String refreshToken);

  // Redis 에서 uid 값으로 Token 추출
  Optional<Token> findByUid(String uid);

}