package com.thiadmar.smartmeter.controller;

import com.thiadmar.smartmeter.exceptions.ReadingNotFoundException;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import com.thiadmar.smartmeter.service.InitialiseRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Optional;

@RestController
public class SmartMeterApiController {

    @Autowired
    private ReadingRepository readingRepository;

    @Autowired
    private InitialiseRepositoryService initialiseRepositoryService;

    @PostConstruct
    private void postConstruct() {
        int numRecords = 10;
        int numInnerRecords = 3;
        initialiseRepositoryService.initialiseRepository(numRecords, numInnerRecords);
    }

    @GetMapping("/api/smart/reads/{accountNumber}")
    public Reading getReading(@PathVariable Long accountNumber) {
        Optional<Reading> reading = readingRepository.findById(accountNumber);
        if (reading.isEmpty()) {
            throw new ReadingNotFoundException("Could not find account with account id '"+accountNumber+"' in repository.");
        } else {
            return reading.get();
        }
    }
}
