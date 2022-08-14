package com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel;

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

    public static PaymentResponse defaultFallBack(String payNo){
        return PaymentResponse.builder().code(-3).msg("未获取支付").payNo(payNo).paymentId(null).build();
    }
}
