package com.thoughtworks.midquiz.midquiz.infraustruct.client.fallback;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentResponse;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PaymentProviderBackFactory implements FallbackFactory<PaymentClient> {

    @Override
    public PaymentClient create(Throwable throwable) {
        return new PaymentClient() {
            @Override
            public PaymentResponse payment(PaymentRequest paymentRequest) {
                return PaymentResponse.defaultFallBack(paymentRequest.getTransactionNo());
            }

            @Override
            public PaymentResponse searchPayment(String transactionNo) {
                return null;
            }

            @Override
            public PaymentResponse refundPayment(String transactionNo) {
                return null;
            }
        };
    }
}