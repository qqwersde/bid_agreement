package com.thoughtworks.midquiz.midquiz.response;

import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.request.DepositInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepositPaymentResponse {

    private String message;

    private Integer code;

    private String paymentId;

    public static DepositPaymentResponse build(PaymentResponse paymentResponse){
        return DepositPaymentResponse.builder().message(paymentResponse.getMsg())
                .code(paymentResponse.getCode()).paymentId(paymentResponse.getPaymentId()).build();
    }

}
