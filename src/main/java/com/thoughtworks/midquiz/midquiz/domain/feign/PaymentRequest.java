package com.thoughtworks.midquiz.midquiz.domain.feign;

import com.thoughtworks.midquiz.midquiz.request.DepositInfoDTO;
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
public class PaymentRequest {
    private String payerPhone;
    private String payerAccount;
    private String payerName;
    private double depositAmount;
    private String transactionNo;

    public static PaymentRequest init(String payNo,DepositInfoDTO depositInfoDTO) {
        return PaymentRequest.builder().depositAmount(depositInfoDTO.getDepositAmount())
                .payerAccount(depositInfoDTO.getPayerAccount())
                .payerPhone(depositInfoDTO.getPayerPhone())
                .payerName(depositInfoDTO.getPayerName())
                .transactionNo(payNo)
                .build();
    }

}
