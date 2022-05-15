package com.thoughtworks.midquiz.midquiz.client;

import com.thoughtworks.midquiz.midquiz.client.config.PaymentConfig;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-client", url = "${application.payment.url}", configuration = PaymentConfig.class)
public interface PaymentClient {
    @PostMapping("/payment")
    PaymentResponse payment(@RequestBody PaymentRequest paymentRequest);

    @GetMapping("/payment")
    PaymentResponse searchPayment(@RequestParam("transactionNo") String transactionNo);

    @PostMapping("/payment/refund")
    PaymentResponse refundPayment(@RequestParam("transactionNo") String transactionNo);
}
