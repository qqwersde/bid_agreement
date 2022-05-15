package com.thoughtworks.midquiz.midquiz.repo;

import com.thoughtworks.midquiz.midquiz.entity.BidAgreement;
import com.thoughtworks.midquiz.midquiz.entity.DepositPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepositPaymentRepository extends JpaRepository<DepositPayment, Long> {


}
