package com.thoughtworks.midquiz.midquiz.serveice;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.AuctionObjectClient;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.PaymentClient;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.AuctionObjectInfo;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.AuctionStatus;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.PaymentResponse;
import com.thoughtworks.midquiz.midquiz.infraustruct.repo.po.BidAgreement;
import com.thoughtworks.midquiz.midquiz.infraustruct.repo.po.BidAgreementStatus;
import com.thoughtworks.midquiz.midquiz.infraustruct.repo.po.DepositPayment;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import com.thoughtworks.midquiz.midquiz.infraustruct.repo.BidAgreementRepository;
import com.thoughtworks.midquiz.midquiz.infraustruct.repo.DepositPaymentRepository;
import com.thoughtworks.midquiz.midquiz.infraustruct.servicedegradation.PaymentClientDegradation;
import com.thoughtworks.midquiz.midquiz.serveice.dto.request.DepositPaymentRequest;
import com.thoughtworks.midquiz.midquiz.serveice.dto.response.DepositPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final BidAgreementRepository bidAgreementRepository;

    private final DepositPaymentRepository depositPaymentRepository;

    private final AuctionObjectClient auctionObjectClient;

    private final PaymentClient paymentClient;

    private final PaymentClientDegradation paymentClientDegradation;

    @Transactional
    public DepositPaymentResponse payDeposit(Long baid, DepositPaymentRequest depositPaymentRequest) {
        BidAgreement bidAgreement = bidAgreementRepository.findById(baid).orElseThrow(() ->
                new BusinessException("bid agreement not found:" + baid, HttpStatus.NOT_FOUND));
        if (!bidAgreement.getStatus().equals(BidAgreementStatus.PENDING.name())) {
            throw new BusinessException("this bid agreement cant not pay deposit", HttpStatus.BAD_REQUEST);
        }
        AuctionObjectInfo auctionObjectInfo = auctionObjectClient.searchAuctionTarget(depositPaymentRequest.getAuctionNum());
        if (Objects.isNull(auctionObjectInfo)) {
            throw new BusinessException("this auction target is not exit:" + depositPaymentRequest.getAuctionNum(), HttpStatus.NOT_FOUND);
        } else if (AuctionStatus.isUnAvailable(auctionObjectInfo.getAuctionStatus())) {
            throw new BusinessException("this auction object is unavailable:" + depositPaymentRequest.getAuctionNum(), HttpStatus.BAD_REQUEST);
        }
        String transactionNo = UUID.randomUUID().toString();
        PaymentRequest paymentRequest = PaymentRequest.init(transactionNo, depositPaymentRequest.getDepositInfoDTO());
        PaymentResponse paymentResponse;
        paymentResponse = paymentClient.payment(paymentRequest);
        //id为null并且code=-3说明是hytrix的fallback返回的默认结果，分区才会执行
        if (paymentResponse.getPaymentId() == null && paymentResponse.getCode() == -3) {
            paymentResponse = paymentClientDegradation.getPaymentResponse(transactionNo);
        }
        saveRecord(bidAgreement, paymentRequest, paymentResponse);
        return DepositPaymentResponse.build(paymentResponse);
    }

    private void saveRecord(BidAgreement bidAgreement, PaymentRequest paymentRequest, PaymentResponse payment) {
        try {
            DepositPayment depositPayment = DepositPayment.success(payment, paymentRequest);
            depositPaymentRepository.save(depositPayment);
            bidAgreement.setStatus(BidAgreementStatus.PROCESSING.name());
            bidAgreementRepository.save(bidAgreement);
        } catch (Exception e) {
            paymentClient.refundPayment(paymentRequest.getTransactionNo());
            throw new BusinessException("系统异常，请检查退款信息", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
