package com.thiadmar.smartmeter.model.response;

public class InnerReadingResponse {

    private Long id;
    private Long meterId;
    private long usageSinceLastRead;
    private long periodSinceLastRead;

    public InnerReadingResponse(Long id, Long meterId, long usageSinceLastRead, long periodSinceLastRead) {
        this.id = id;
        this.meterId = meterId;
        this.usageSinceLastRead = usageSinceLastRead;
        this.periodSinceLastRead = periodSinceLastRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public long getUsageSinceLastRead() {
        return usageSinceLastRead;
    }

    public void setUsageSinceLastRead(long usageSinceLastRead) {
        this.usageSinceLastRead = usageSinceLastRead;
    }

    public long getPeriodSinceLastRead() {
        return periodSinceLastRead;
    }

    public void setPeriodSinceLastRead(long periodSinceLastRead) {
        this.periodSinceLastRead = periodSinceLastRead;
    }
}
