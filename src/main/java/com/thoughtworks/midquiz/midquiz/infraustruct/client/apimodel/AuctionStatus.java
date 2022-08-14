package com.thoughtworks.midquiz.midquiz.infraustruct.client.apimodel;

public enum AuctionStatus {
    DEALED,
    CANCELLATION,
    RECALL,
    AVAILABLE;

    public static boolean isUnAvailable(AuctionStatus auctionStatus){
        return !AVAILABLE.equals(auctionStatus);
    }

}
