package org.jnjeaaaat.onbition.domain.repository.pay;

import org.jnjeaaaat.onbition.domain.entity.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 결제 현황 repository
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
