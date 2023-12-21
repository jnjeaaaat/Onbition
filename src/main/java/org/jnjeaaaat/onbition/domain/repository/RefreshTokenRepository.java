package org.jnjeaaaat.onbition.domain.repository;

import java.util.Optional;
import org.jnjeaaaat.onbition.domain.entity.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * RefreshToken Repository
 */
@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  // 토큰값으로 refreshToken 추출
  Optional<RefreshToken> findByToken(String token);

  // uid 값으로 refreshToken 추출
  Optional<RefreshToken> findByUid(String uid);

}