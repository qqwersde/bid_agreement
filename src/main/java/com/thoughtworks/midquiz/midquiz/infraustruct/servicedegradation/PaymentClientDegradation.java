package com.thoughtworks.midquiz.midquiz.infraustruct.servicedegradation;

import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentClientDegradation {

    private final PaymentClient paymentClient;

    public PaymentResponse getPaymentResponse(String transactionNo) {
        PaymentResponse payment;
        payment = paymentClient.searchPayment(transactionNo);
        if (payment.getCode() != 0) {
            throw new BusinessException("支付失败，请重试", HttpStatus.BAD_REQUEST);
        }
        return payment;
    }


}
