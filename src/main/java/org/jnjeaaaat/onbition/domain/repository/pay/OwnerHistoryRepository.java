package org.jnjeaaaat.onbition.domain.repository.pay;

import org.jnjeaaaat.onbition.domain.entity.pay.OwnerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 그림 소유 history Repository
 */
@Repository
public interface OwnerHistoryRepository extends JpaRepository<OwnerHistory, Long> {

}
