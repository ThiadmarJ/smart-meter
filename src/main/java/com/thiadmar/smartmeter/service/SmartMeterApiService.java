package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.exceptions.ReadingNotFoundException;
import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.GasReading;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.model.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SmartMeterApiService {

    private final ReadingRepository readingRepository;

    public SmartMeterApiService(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public Reading retrieveReading(Long accountNumber) {
        Optional<Reading> reading = readingRepository.findById(accountNumber);
        if (reading.isEmpty()) {
            throw new ReadingNotFoundException("Could not find account with account id '" + accountNumber + "' in repository.");
        } else {
            return reading.get();
        }
    }

    public ReadingResponse receiveReading(Reading reading) {

        List<ElecReading> updatedElecReadings = new ArrayList<>();
        List<GasReading> updatedGasReadings = new ArrayList<>();
        if (reading.getElecReadings() != null) {
            reading.getElecReadings().forEach(elecReading -> elecReading.setReadingMain(reading));
            updatedElecReadings.addAll(reading.getElecReadings());
        }
        if (reading.getGasReadings() != null) {
            reading.getGasReadings().forEach(gasReading -> gasReading.setReadingMain(reading));
            updatedGasReadings.addAll(reading.getGasReadings());
        }
        Optional<Reading> oldReadingRecord = readingRepository.findById(reading.getAccountId());

        if (oldReadingRecord.isPresent()) {
            updatedElecReadings.addAll(oldReadingRecord.get().getElecReadings());
            updatedGasReadings.addAll(oldReadingRecord.get().getGasReadings());
        }
        reading.setElecReadings(updatedElecReadings);
        reading.setGasReadings(updatedGasReadings);

        Reading cleanReading = duplicateCheck(reading);
        readingRepository.save(cleanReading);
        return new ReadingResponse(reading.getAccountId());
    }

    private Reading duplicateCheck(Reading reading) {
        Reading cleanReading = reading;
        ElecReading curElecReading;
        for (int i = 0; i < reading.getElecReadings().size(); i++) {
            curElecReading = reading.getElecReadings().get(i);
            for (int j = i + 1; j < reading.getElecReadings().size(); j++) {
                ElecReading elecReadingToProcess = reading.getElecReadings().get(j);
                if (curElecReading.getMeterId() == elecReadingToProcess.getMeterId()) {
                    if (curElecReading.getDate().compareTo(elecReadingToProcess.getDate()) == 0) {
                        //dates are equal
                        cleanReading.getElecReadings().remove(curElecReading);
                        i--;
                        j = reading.getElecReadings().size();
                    }
                }
            }
        }
        GasReading curGasReading;
        for (int i = 0; i < reading.getGasReadings().size(); i++) {
            curGasReading = reading.getGasReadings().get(i);
            for (int j = i + 1; j < reading.getGasReadings().size(); j++) {
                GasReading gasReadingToProcess = reading.getGasReadings().get(j);
                if (curGasReading.getMeterId() == gasReadingToProcess.getMeterId()) {
                    if (curGasReading.getDate().compareTo(gasReadingToProcess.getDate()) == 0) {
                        cleanReading.getGasReadings().remove(curGasReading);
                        i--;
                        j = reading.getGasReadings().size();
                    }
                }
            }
        }
        return cleanReading;
    }

}
