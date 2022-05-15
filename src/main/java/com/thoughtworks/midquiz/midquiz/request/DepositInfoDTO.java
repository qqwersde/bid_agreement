package com.thoughtworks.midquiz.midquiz.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepositInfoDTO {

    @Size(min=11,max = 11)
    private String payerPhone;
    private String payerAccount;
    private String payerName;
    private double depositAmount;

}
