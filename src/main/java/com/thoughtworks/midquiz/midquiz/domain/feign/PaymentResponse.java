package com.thoughtworks.midquiz.midquiz.domain.feign;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private Integer code;
    private String msg;
    private String payNo;
    private String paymentId;
}
