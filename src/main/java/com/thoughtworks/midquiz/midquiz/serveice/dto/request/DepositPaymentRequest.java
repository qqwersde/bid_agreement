package com.thoughtworks.midquiz.midquiz.serveice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class DepositPaymentRequest {

    private String auctionNum;

    @Valid
    private DepositInfoDTO depositInfoDTO;
}
