package org.jnjeaaaat.onbition.domain.repository.pay;

import java.util.List;
import org.jnjeaaaat.onbition.domain.entity.User;
import org.jnjeaaaat.onbition.domain.entity.pay.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 유저 잔액 repository
 */
@Repository
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

  // 해당 유저의 입출금 목록 Id값으로 내림차순
  List<UserBalance> findAllByUserOrderByIdDesc(User user);

}
