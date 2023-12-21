package org.jnjeaaaat.onbition.domain.repository;

import java.util.Optional;
import org.jnjeaaaat.onbition.domain.entity.token.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * AccessToken Repository
 */
@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

  // Redis 에서 Token 값으로 AccessToken 추출
  Optional<AccessToken> findByToken(String token);

  // Redis 에서 uid 값으로 AccessToken 추출
  Optional<AccessToken> findByUid(String uid);

}