package com.thoughtworks.midquiz.midquiz.infraustruct.client;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.config.AuctionConfig;
import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.AuctionObjectInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auction-client", url = "${application.auction.url}", configuration = AuctionConfig.class)
public interface AuctionObjectClient {

    @GetMapping("/auctions/{auctionNum}")
    AuctionObjectInfo searchAuctionTarget(@PathVariable("auctionNum") String auctionNum);
}
