package com.thoughtworks.midquiz.midquiz.controller;


import com.thoughtworks.midquiz.midquiz.serveice.DepositService;
import com.thoughtworks.midquiz.midquiz.serveice.dto.request.DepositPaymentRequest;
import com.thoughtworks.midquiz.midquiz.serveice.dto.response.DepositPaymentResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/bid_agreements/{baid}/deposit_payment")
public class DepositController {


    private final DepositService depositService;


    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @PostMapping("")
    public DepositPaymentResponse payDeposit(@PathVariable("baid") Long baid, @RequestBody @Valid DepositPaymentRequest depositPaymentRequest) {
        return depositService.payDeposit(baid,depositPaymentRequest);
    }


}
