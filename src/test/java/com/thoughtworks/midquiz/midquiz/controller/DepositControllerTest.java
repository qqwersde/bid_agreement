package com.thoughtworks.midquiz.midquiz.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.midquiz.midquiz.Serveice.DepositService;
import com.thoughtworks.midquiz.midquiz.domain.feign.AuctionObjectInfo;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.entity.BidAgreement;
import com.thoughtworks.midquiz.midquiz.entity.BidAgreementStatus;
import com.thoughtworks.midquiz.midquiz.exception.ErrorResult;
import com.thoughtworks.midquiz.midquiz.request.DepositInfoDTO;
import com.thoughtworks.midquiz.midquiz.request.DepositPaymentRequest;
import com.thoughtworks.midquiz.midquiz.response.DepositPaymentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;

import static com.thoughtworks.midquiz.midquiz.domain.feign.AuctionStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class DepositControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepositService depositService;

    @Test
    public void should_pay_deposit_successful_when_auction_and_agreement_exist_given_deposit_info_and_auction_number() throws Exception {
        String auctionNumber = "100";
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        String requestJson = JSONObject.toJSONString(request);
        DepositPaymentResponse depositPaymentResponse = DepositPaymentResponse.builder().paymentId("P001").code(0).message("payment build").build();

        when(depositService.payDeposit(1L,request)).thenReturn(depositPaymentResponse);
        MvcResult result = mockMvc.perform(
                post("/bid_agreements/1/deposit_payment")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        DepositPaymentResponse paymentResponse = JSON.parseObject(content, DepositPaymentResponse.class);
        assertEquals(paymentResponse.getPaymentId(), "P001");
    }

    @Test
    public void should_catch_exception_when_auction_and_agreement_exist_given_deposit_info_is_wrong_and_auction_number() throws Exception {
        String auctionNumber = "100";
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("1512345678").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        String requestJson = JSONObject.toJSONString(request);
        DepositPaymentResponse depositPaymentResponse = DepositPaymentResponse.builder().paymentId("P001").code(0).message("payment build").build();

        MvcResult result = mockMvc.perform(
                post("/bid_agreements/1/deposit_payment")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        ErrorResult response = JSON.parseObject(content, ErrorResult.class);
        assertEquals(response.getErrorCode(), 400);
    }
}