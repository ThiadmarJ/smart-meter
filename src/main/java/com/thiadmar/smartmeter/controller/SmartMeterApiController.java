package com.thiadmar.smartmeter.controller;

import com.thiadmar.smartmeter.model.reading.Reading;
import com.thiadmar.smartmeter.model.response.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import com.thiadmar.smartmeter.service.InitialiseRepositoryService;
import com.thiadmar.smartmeter.service.SmartMeterApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class SmartMeterApiController {

    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private InitialiseRepositoryService initialiseRepositoryService;

    private SmartMeterApiService smartMeterApiService;

    @PostConstruct
    private void postConstruct() {
        int numRecords = 10;
        int numInnerRecords = 3;
        initialiseRepositoryService.initialiseRepository(numRecords, numInnerRecords);
        smartMeterApiService = new SmartMeterApiService(readingRepository);
    }

    @GetMapping("/api/smart/reads/{accountNumber}")
    public Reading getReading(@PathVariable Long accountNumber) {
        return smartMeterApiService.retrieveReading(accountNumber);
    }

    @PostMapping("/api/smart/reads")
    public ReadingResponse postReading(@RequestBody Reading reading) {
        return smartMeterApiService.receiveReading(reading);
    }
}
