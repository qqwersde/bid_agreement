package com.thoughtworks.midquiz.midquiz.infraustruct.repo;

import com.thoughtworks.midquiz.midquiz.infraustruct.repo.po.DepositPayment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class DepositPaymentRepositoryTest {

    @Autowired
    private DepositPaymentRepository depositPaymentRepository;

    @Test
    public void should_save_bid_agreement_successfully() {
        DepositPayment depositPayment = DepositPayment.builder().depositAmount(111.11).paymentId("1001").payNo("P001").status("FINISH").payerName("user").payerAccount("1001")
                .payerPhone("15122345678").build();
        DepositPayment save = depositPaymentRepository.save(depositPayment);
        assertEquals(save.getId(),1L);

    }
}