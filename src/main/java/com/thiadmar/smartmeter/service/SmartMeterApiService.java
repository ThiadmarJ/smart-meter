package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.exceptions.ReadingNotFoundException;
import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.model.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmartMeterApiService {

    @Autowired
    private ReadingRepository readingRepository;

    public SmartMeterApiService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public Reading retrieveReading(Long accountNumber) {
        Optional<Reading> reading = readingRepository.findById(accountNumber);
        if (reading.isEmpty()) {
            throw new ReadingNotFoundException("Could not find account with account id '"+accountNumber+"' in repository.");
        } else {
            return reading.get();
        }
    }

    public ReadingResponse receiveReading(Reading reading) {
        Reading readingToSave = new Reading(reading.getAccountId());
        List<ElecReading> elecReadings = reading.getElecReadings().stream().toList();
        for (int i = 0; i < elecReadings.size(); i++) {
            System.out.println(elecReadings.get(i).getReading() + "ahhahahhaha");
        }

        reading.setElecReadings(reading.getElecReadings().stream().toList());
        reading.setGasReadings(reading.getGasReadings().stream().toList());;
        readingRepository.save(reading);
        return new ReadingResponse(reading.getAccountId());
    }

}
