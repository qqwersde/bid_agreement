package com.thoughtworks.midquiz.midquiz.Serveice;

import com.thoughtworks.midquiz.midquiz.client.AuctionObjectClient;
import com.thoughtworks.midquiz.midquiz.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.domain.feign.AuctionObjectInfo;
import com.thoughtworks.midquiz.midquiz.domain.feign.AuctionStatus;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.entity.BidAgreement;
import com.thoughtworks.midquiz.midquiz.entity.BidAgreementStatus;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import com.thoughtworks.midquiz.midquiz.scheduler.PaymentScheduler;
import com.thoughtworks.midquiz.midquiz.repo.BidAgreementRepository;
import com.thoughtworks.midquiz.midquiz.repo.DepositPaymentRepository;
import com.thoughtworks.midquiz.midquiz.request.DepositInfoDTO;
import com.thoughtworks.midquiz.midquiz.request.DepositPaymentRequest;
import com.thoughtworks.midquiz.midquiz.response.DepositPaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.thoughtworks.midquiz.midquiz.domain.feign.AuctionStatus.AVAILABLE;
import static com.thoughtworks.midquiz.midquiz.domain.feign.AuctionStatus.RECALL;
import static com.thoughtworks.midquiz.midquiz.entity.BidAgreementStatus.PROCESSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class DepositServiceTest {

    @Mock
    private  BidAgreementRepository bidAgreementRepository;

    @Mock
    private  DepositPaymentRepository depositPaymentRepository;

    @Mock
    private  AuctionObjectClient auctionObjectClient;

    @Mock
    private  PaymentClient paymentClient;

    @Mock
    private PaymentScheduler paymentScheduler;

    @InjectMocks
    private DepositService depositService;

    @Test
    void should_pay_deposit_successful_when_auction_and_agreement_exist_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        String paymentId = "P001";
        String payNo = "payNo001";
        Long baid = 1L;
        LocalDateTime auctionTime = LocalDateTime.of(2022, 5, 15, 15, 0, 0);
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        AuctionObjectInfo auctionObjectInfo = stubAuctionObjectInfo(auctionNumber, AVAILABLE, auctionTime);
        PaymentResponse paymentResponse = stubPaymentResponse(paymentId, payNo);
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(auctionObjectInfo);
        when(paymentClient.payment(any(PaymentRequest.class))).thenReturn(paymentResponse);

        DepositPaymentResponse depositPaymentResponse = depositService.payDeposit(baid, request);

        assertEquals(depositPaymentResponse.getMessage(), "build");
        assertEquals(depositPaymentResponse.getPaymentId(), paymentId);
        verify(bidAgreementRepository, times(1)).save(any());
        verify(depositPaymentRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_when_agreement_is_not_pending_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        BidAgreement bidAgreement = stubBidAgreement(PROCESSING.name());

        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));

        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));
        assertEquals("this bid agreement cant not pay deposit", exception.getMessage());
        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, never()).searchAuctionTarget(auctionNumber);
        verify(paymentClient, never()).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_agreement_not_exist_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        BidAgreement bidAgreement = stubBidAgreement(PROCESSING.name());

        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));
        assertEquals("bid agreement not found:1", exception.getMessage());
        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, never()).searchAuctionTarget(auctionNumber);
        verify(paymentClient, never()).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_auction_not_exist_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(null);

        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));

        assertEquals("this auction target is not exit:100", exception.getMessage());
        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, times(1)).searchAuctionTarget(auctionNumber);
        verify(paymentClient, never()).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_auction_is_not_available_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        LocalDateTime auctionTime = LocalDateTime.of(2022, 5, 15, 15, 0, 0);
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        AuctionObjectInfo auctionObjectInfo = stubAuctionObjectInfo(auctionNumber, RECALL, auctionTime);
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(auctionObjectInfo);


        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));

        assertEquals("this auction object is unavailable:100", exception.getMessage());

        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, times(1)).searchAuctionTarget(auctionNumber);
        verify(paymentClient, never()).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_auction_client_throw_exception_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenThrow(new BusinessException("好像出错了，请稍后再试", HttpStatus.INTERNAL_SERVER_ERROR));


        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));

        assertEquals("好像出错了，请稍后再试", exception.getMessage());

        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, times(1)).searchAuctionTarget(auctionNumber);
        verify(paymentClient, never()).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_throw_exception_when_payment_client_throw_exception_given_deposit_info_is_wrong_and_auction_number(){
        String auctionNumber = "100";
        Long baid = 1L;
        LocalDateTime auctionTime = LocalDateTime.of(2022, 5, 15, 15, 0, 0);
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        AuctionObjectInfo auctionObjectInfo = stubAuctionObjectInfo(auctionNumber, AVAILABLE, auctionTime);
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        BusinessException businessException = new BusinessException("支付失败，请检查账户", HttpStatus.BAD_REQUEST);


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(auctionObjectInfo);
        when(paymentClient.payment(any(PaymentRequest.class))).thenThrow(businessException);
        when(paymentScheduler.getPaymentResponse(anyString(),any(BusinessException.class
        ))).thenThrow(new BusinessException("支付失败，请检查账户", HttpStatus.BAD_REQUEST));



        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));

        assertEquals("支付失败，请检查账户", exception.getMessage());

        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, times(1)).searchAuctionTarget(auctionNumber);
        verify(paymentClient, times(1)).payment(any());
        verify(bidAgreementRepository, never()).save(any());
        verify(depositPaymentRepository, never()).save(any());
    }

    @Test
    void should_return_payment_response_when_payment_client_throw_500_exception_given_scheduler_return_response(){
        String auctionNumber = "100";
        String paymentId = "P001";
        String payNo = "payNo001";
        Long baid = 1L;
        LocalDateTime auctionTime = LocalDateTime.of(2022, 5, 15, 15, 0, 0);
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        AuctionObjectInfo auctionObjectInfo = stubAuctionObjectInfo(auctionNumber, AVAILABLE, auctionTime);
        PaymentResponse paymentResponse = stubPaymentResponse(paymentId, payNo);
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();
        BusinessException businessException = new BusinessException("支付失败，请检查账户", HttpStatus.BAD_REQUEST);


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(auctionObjectInfo);
        when(paymentClient.payment(any(PaymentRequest.class))).thenThrow(businessException);
        when(paymentScheduler.getPaymentResponse(anyString(),any(BusinessException.class
        ))).thenReturn(paymentResponse);


        DepositPaymentResponse depositPaymentResponse = depositService.payDeposit(baid, request);

        assertEquals(depositPaymentResponse.getMessage(), "build");
        assertEquals(depositPaymentResponse.getPaymentId(), paymentId);

        verify(bidAgreementRepository, times(1)).findById(baid);
        verify(auctionObjectClient, times(1)).searchAuctionTarget(auctionNumber);
        verify(paymentClient, times(1)).payment(any());
        verify(bidAgreementRepository, times(1)).save(any());
        verify(depositPaymentRepository, times(1)).save(any());
    }

    @Test
    void should_throw_exception_when_repository_throw_exception_given_deposit_info_and_auction_number(){
        String auctionNumber = "100";
        String paymentId = "P001";
        String transactionNo = "payNo001";
        Long baid = 1L;
        LocalDateTime auctionTime = LocalDateTime.of(2022, 5, 15, 15, 0, 0);
        BidAgreement bidAgreement = stubBidAgreement(BidAgreementStatus.PENDING.name());
        AuctionObjectInfo auctionObjectInfo = stubAuctionObjectInfo(auctionNumber, AVAILABLE, auctionTime);
        PaymentResponse paymentResponse = stubPaymentResponse(paymentId, transactionNo);
        DepositInfoDTO depositInfoDTO = DepositInfoDTO.builder().depositAmount(11.33).payerAccount("376633").payerName("user").payerPhone("15123456789").build();
        DepositPaymentRequest request = DepositPaymentRequest.builder().auctionNum(auctionNumber).depositInfoDTO(depositInfoDTO).build();


        when(bidAgreementRepository.findById(baid)).thenReturn(Optional.of(bidAgreement));
        when(auctionObjectClient.searchAuctionTarget(auctionNumber)).thenReturn(auctionObjectInfo);
        when(paymentClient.payment(any(PaymentRequest.class))).thenReturn(paymentResponse);
        when(bidAgreementRepository.save(any())).thenThrow(new BusinessException("save fail"));

        Exception exception = assertThrows(
                BusinessException.class,
                () -> depositService.payDeposit(baid, request));

        assertEquals("系统异常，请检查退款信息", exception.getMessage());
        verify(paymentClient,times(1)).refundPayment(anyString());
    }

    private PaymentResponse stubPaymentResponse(String paymentId, String payNo) {
        return PaymentResponse.builder().paymentId(paymentId).code(0).msg("build").payNo(payNo).build();
    }

    private AuctionObjectInfo stubAuctionObjectInfo(String auctionNumber, AuctionStatus available, LocalDateTime auctionTime) {
        return AuctionObjectInfo.builder().auctionNum(auctionNumber).auctionStatus(available).auctionTime(auctionTime).build();
    }

    private BidAgreement stubBidAgreement(String status) {
        return BidAgreement.builder().id(1L).status(status).build();
    }
}