package com.echovue.speed.data;

import com.echovue.speed.model.ReverseGeoCoderFullAddressWithSpeed;
import com.google.common.collect.ImmutableList;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderFullAddress;
import com.tomtom.online.sdk.search.data.reversegeocoder.ReverseGeocoderSearchResponse;

public class ReverseGeoCoderSearchResponseWithSpeed extends ReverseGeocoderSearchResponse {
    protected ReverseGeoCoderFullAddressWithSpeed[] addresses;

    public ImmutableList<ReverseGeocoderFullAddress> getAddresses() {
        return this.hasResults() ? ImmutableList.copyOf(this.addresses) : ImmutableList.of();
    }
}
