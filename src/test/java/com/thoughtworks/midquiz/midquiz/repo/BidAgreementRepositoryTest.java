package com.thoughtworks.midquiz.midquiz.repo;

import com.thoughtworks.midquiz.midquiz.entity.BidAgreement;
import com.thoughtworks.midquiz.midquiz.entity.BidAgreementStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class BidAgreementRepositoryTest {

    @Autowired
    private BidAgreementRepository bidAgreementRepository;

    @Test
    public void should_save_bid_agreement_successfully() {
        BidAgreement bidAgreement = BidAgreement.builder().status(BidAgreementStatus.PENDING.name()).build();
        BidAgreement save = bidAgreementRepository.save(bidAgreement);
        assertEquals(save.getId(),1L);

    }

    @Test
    @Sql(value = {"classpath:sql/INSERT_TABLE_BID_AGREEMENT_DATA.sql"})
    public void should_find_bid_agreement_by_id(){
        Optional<BidAgreement> bidAgreement = bidAgreementRepository.findById(1L);
        assertTrue(bidAgreement.isPresent());
        assertEquals(bidAgreement.get().getStatus(),"PENDING");
    }

}