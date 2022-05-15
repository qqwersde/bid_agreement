package com.thoughtworks.midquiz.midquiz.entity;

import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentRequest;
import com.thoughtworks.midquiz.midquiz.domain.feign.PaymentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class DepositPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String payerPhone;
    private String payerAccount;
    private String payerName;
    private double depositAmount;
    private String payNo;
    private String status;
    private String paymentId;


    @CreatedDate
    private LocalDateTime createTime;
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public static DepositPayment success(PaymentResponse paymentResponse, PaymentRequest paymentRequest){
       return DepositPayment.builder().depositAmount(paymentRequest.getDepositAmount()).paymentId(paymentResponse.getPaymentId())
                .payerPhone(paymentRequest.getPayerPhone()).payNo(paymentRequest.getTransactionNo()).payerAccount(paymentRequest.getPayerAccount())
                .payerName(paymentRequest.getPayerName()).status(PayStatus.FINISH.name()).build();
    }
}
