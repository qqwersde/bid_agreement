package com.thoughtworks.midquiz.midquiz.infraustruct.client;

import com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.AuctionObjectInfo;
import com.thoughtworks.midquiz.midquiz.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testng.annotations.Ignore;

import static com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel.AuctionStatus.AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Ignore
class AuctionObjectClientTest {

    @Autowired
    private AuctionObjectClient auctionObjectClient;

    @Test
    void should_get_auction_object_when_object_exist_given_auction_number(){
        AuctionObjectInfo auctionObjectInfo = auctionObjectClient.searchAuctionTarget("A001");
        assertEquals(auctionObjectInfo.getAuctionStatus(),AVAILABLE);
    }

    @Test
    void should_get_null_when_object_not_exist_given_auction_number(){
        AuctionObjectInfo auctionObjectInfo = auctionObjectClient.searchAuctionTarget("A002");
        assertNull(auctionObjectInfo);
    }

    @Test
    void should_throw_exception_when_client_throw_exception_given_auction_number(){

        Exception exception = assertThrows(
                BusinessException.class,
                () -> auctionObjectClient.searchAuctionTarget("B002"));

        assertEquals("好像出错了，请稍后再试", exception.getMessage());
    }
}