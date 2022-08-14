package com.thoughtworks.midquiz.midquiz.infraustruct.client;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Ignore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@Ignore
class PaymentClientTest {

    @Autowired
    private PaymentClient paymentClient;

    @Test
    void should_throw_exception_when_payment_info_param_is_wrong_given_payment_info(){
        PaymentRequest paymentRequest = PaymentRequest.builder().transactionNo("P001").payerPhone("11211")
                .depositAmount(111.11).payerAccount("7758").build();
        Exception exception = assertThrows(
                BusinessException.class,
                () -> paymentClient.payment(paymentRequest));
        assertEquals("支付失败，请检查账户", exception.getMessage());
    }

    @Test
    void should_get_payment_result_when_payment_given_payment_info(){
        PaymentRequest paymentRequest = PaymentRequest.builder().transactionNo("P001").payerPhone("15123456789")
                .depositAmount(111.11).payerAccount("7758").build();
        PaymentResponse payment = paymentClient.payment(paymentRequest);
        assertEquals(payment.getCode(), 0);
    }

    @Test
    void should_get_payment_degradation_result_when_payment_given_payment_info(){
        PaymentRequest paymentRequest = PaymentRequest.builder().transactionNo("P001").payerPhone("15123456789")
                .depositAmount(111.11).payerAccount("665566").build();
        PaymentResponse payment = paymentClient.payment(paymentRequest);
        assertEquals(payment.getCode(), -3);
    }

    @Test
    void should_get_payment_result_when_search_payment_given_transaction_no(){
        PaymentResponse payment = paymentClient.searchPayment("P001");
        assertEquals(payment.getCode(), 0);
    }

    @Test
    void should_refund__given_transaction_no(){
        PaymentResponse payment = paymentClient.refundPayment("P001");
        assertEquals(payment.getCode(), 0);
    }



}