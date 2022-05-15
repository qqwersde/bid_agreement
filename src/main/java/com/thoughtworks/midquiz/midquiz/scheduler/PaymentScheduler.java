package com.thoughtworks.midquiz.midquiz.scheduler;

import com.thoughtworks.midquiz.midquiz.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentScheduler {

    private final PaymentClient paymentClient;

    public PaymentResponse getPaymentResponse(String transactionNo, BusinessException exception) {
        PaymentResponse payment;
        if (exception.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            payment = paymentClient.searchPayment(transactionNo);
            if (payment.getCode()!=0){
                throw new BusinessException("支付失败，请重试", HttpStatus.BAD_REQUEST);
            }
        } else {
            throw exception;
        }
        return payment;
    }


}
