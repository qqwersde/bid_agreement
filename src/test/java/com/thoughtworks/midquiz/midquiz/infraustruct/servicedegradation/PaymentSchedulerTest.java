package com.thoughtworks.midquiz.midquiz.infraustruct.servicedegradation;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PaymentSchedulerTest {

    @Mock
    private PaymentClient paymentClient;

    @InjectMocks
    private PaymentClientDegradation paymentClientDegradation;


    @Test
    void should_throw_exception_when_condition_is_internal_error(){
        BusinessException businessException = new BusinessException("支付异常", HttpStatus.INTERNAL_SERVER_ERROR);
        String transactionNo = "P001";
        PaymentResponse paymentResponse = PaymentResponse.builder().paymentId("001").payNo(transactionNo).code(0).msg("支付成功").build();

        when(paymentClient.searchPayment(transactionNo)).thenReturn(paymentResponse);

        PaymentResponse response = paymentClientDegradation.getPaymentResponse(transactionNo);
        assertEquals(response.getCode(),0);
    }

    @Test
    void should_throw_exception_when_response_is_failed_payment(){
        String transactionNo = "P001";
        PaymentResponse paymentResponse = PaymentResponse.builder().paymentId("001").payNo(transactionNo).code(-1).msg("支付失败").build();

        when(paymentClient.searchPayment(transactionNo)).thenReturn(paymentResponse);

        Exception exception = assertThrows(
                BusinessException.class,
                () -> paymentClientDegradation.getPaymentResponse(transactionNo));
        assertEquals("支付失败，请重试", exception.getMessage());
    }
}