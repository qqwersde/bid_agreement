package com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuctionObjectInfo {
    private LocalDateTime auctionTime;
    private String auctionNum;
    private AuctionStatus auctionStatus;
}
