package org.jnjeaaaat.onbition.domain.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User JpaRepository
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  // uid 값으로 get User Entity
  User getByUid(String uid);

  // uid 값으로 Optional 타입으로 get User Entity
  Optional<User> findByUid(String uid);

  // uid 값으로 존재하는 유저인지 확인
  boolean existsByUidAndDeletedAt(String uid, LocalDateTime deletedAt);

  // name 이 중복되는지 확인
  boolean existsByNameAndDeletedAt(String name, LocalDateTime deletedAt);

}
