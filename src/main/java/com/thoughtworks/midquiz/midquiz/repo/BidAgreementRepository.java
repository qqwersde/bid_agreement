package com.thoughtworks.midquiz.midquiz.repo;

import com.thoughtworks.midquiz.midquiz.entity.BidAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidAgreementRepository extends JpaRepository<BidAgreement, Long> {


    Optional<BidAgreement> findById(Long id);
}
