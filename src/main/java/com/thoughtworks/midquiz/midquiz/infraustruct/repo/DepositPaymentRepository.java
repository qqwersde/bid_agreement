package com.thoughtworks.midquiz.midquiz.infraustruct.repo;

import com.thoughtworks.midquiz.midquiz.infraustruct.repo.po.DepositPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositPaymentRepository extends JpaRepository<DepositPayment, Long> {


}
