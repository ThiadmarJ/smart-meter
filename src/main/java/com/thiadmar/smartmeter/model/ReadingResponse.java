package com.thiadmar.smartmeter.model;

public class ReadingResponse {

    public ReadingResponse() {
    }

    private Long id;

    public ReadingResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
