package com.echovue.speed.model;

import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderFullAddress;

import java.util.Arrays;

public class ReverseGeoCoderFullAddressWithSpeed extends ReverseGeocoderFullAddress {
    protected String speedLimit;

    public String getSpeedLimit() {
        return this.speedLimit;
    }

    @Override
    public String toString() {
        return "ReverseGeocoderFullAddress(address=" + this.getAddress() + ", speedLimit=" + this.getSpeedLimit() + ", matchType=" + this.getMatchType() + ", roadUse=" + Arrays.deepToString(this.getRoadUse()) + ", position=" + this.getPosition() + ")";
    }
}
