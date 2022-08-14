package com.thoughtworks.midquiz.midquiz.infraustruct.client.config;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.decoder.PaymentErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class PaymentConfig {

    @Bean
    public ErrorDecoder decoder() {
        return new PaymentErrorDecoder();
    }
}
