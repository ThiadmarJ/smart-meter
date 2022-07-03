package com.thiadmar.smartmeter.model.response;

import java.util.List;

public class ReadingResponse {

    private long accountId;
    private List<InnerReadingResponse> readings;

    public ReadingResponse(long accountId, List<InnerReadingResponse> innerReadingResponses) {
        this.accountId = accountId;
        this.readings = innerReadingResponses;
    }

    public ReadingResponse(long accountId) {
        this.accountId = accountId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public List<InnerReadingResponse> getReadings() {
        return readings;
    }

    public void setReadings(List<InnerReadingResponse> readings) {
        this.readings = readings;
    }
}
