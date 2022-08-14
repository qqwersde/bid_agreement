package com.thoughtworks.midquiz.midquiz.infraustruct.client.config;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.decoder.AuctionErrorDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class AuctionConfig {

    @Bean
    public ErrorDecoder decoder() {
        return new AuctionErrorDecoder();
    }

}
