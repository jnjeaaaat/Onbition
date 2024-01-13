package org.jnjeaaaat.onbition.domain.repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jnjeaaaat.onbition.domain.entity.Painting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

/**
 * Painting DB 접근 Repository
 */
@Repository
public interface PaintingRepository extends JpaRepository<Painting, Long> {

  @NotNull
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<Painting> findById(@NotNull Long paintingId);

}
