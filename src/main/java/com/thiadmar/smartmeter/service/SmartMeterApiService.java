package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.exceptions.ReadingNotFoundException;
import com.thiadmar.smartmeter.model.reading.ElecReading;
import com.thiadmar.smartmeter.model.reading.GasReading;
import com.thiadmar.smartmeter.model.reading.Reading;
import com.thiadmar.smartmeter.model.response.InnerReadingResponse;
import com.thiadmar.smartmeter.model.response.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
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
        return new ReadingResponse(reading.getAccountId(), createInnerReadingResponse(cleanReading));
    }

    private List<InnerReadingResponse> createInnerReadingResponse(Reading reading) {
        List<InnerReadingResponse> readingResponses = new ArrayList<>();

        //Elec
        for (int i = 0; i < reading.getElecReadings().size(); i++) {
            ElecReading latestElecReading = reading.getElecReadings().get(i);
            for (int j = i + 1; j < reading.getElecReadings().size(); j++) {
                ElecReading penultimateElecReading = null;
                ElecReading curElecReading = reading.getElecReadings().get(j);
                if (latestElecReading.getMeterId().equals(curElecReading.getMeterId())) {
                    //checking current read is after current latest
                    if (latestElecReading.getDate().after(curElecReading.getDate())) {
                        //checking there is no second last reading or second last reading is before current
                        penultimateElecReading = curElecReading;
                    } else {
                        latestElecReading = curElecReading;
                    }
                }
                if (penultimateElecReading != null) {
                    readingResponses.add(processElecReadingResponse(latestElecReading, penultimateElecReading));
                }
            }
        }

        //Gas
        for (int i = 0; i < reading.getGasReadings().size(); i++) {
            GasReading latestGasReading = reading.getGasReadings().get(i);
            for (int j = i + 1; j < reading.getGasReadings().size(); j++) {
                GasReading penultimateGasReading = null;
                GasReading curGasReading = reading.getGasReadings().get(j);
                if (latestGasReading.getMeterId().equals(curGasReading.getMeterId())) {
                    //checking current read is after current latest
                    if (latestGasReading.getDate().after(curGasReading.getDate())) {
                        //checking there is no second last reading or second last reading is before current
                        penultimateGasReading = curGasReading;
                    } else {
                        latestGasReading = curGasReading;
                    }
                }
                if (penultimateGasReading != null) {
                    readingResponses.add(processGasReadingResponse(latestGasReading, penultimateGasReading));
                }
            }
        }
        return readingResponses;
    }

    private InnerReadingResponse processElecReadingResponse(ElecReading latest, ElecReading penultimate) {
        long id = 0;
        if (penultimate.getId() != null) {
            id = penultimate.getId();
        }
        long meterId = latest.getMeterId();
        long usageSinceLastRead = latest.getReading() - penultimate.getReading();
        long periodSinceLastRead = ChronoUnit.DAYS.between(penultimate.getDate().toInstant(),latest.getDate().toInstant());
        return new InnerReadingResponse(id, meterId, usageSinceLastRead, periodSinceLastRead);
    }

    private InnerReadingResponse processGasReadingResponse(GasReading latest, GasReading penultimate) {
        long id = 0;
        if (penultimate.getId() != null) {
            id = penultimate.getId();
        }
        long meterId = latest.getMeterId();
        long usageSinceLastRead = latest.getReading() - penultimate.getReading();
        long periodSinceLastRead = ChronoUnit.DAYS.between(penultimate.getDate().toInstant(),latest.getDate().toInstant());
        return new InnerReadingResponse(id, meterId, usageSinceLastRead, periodSinceLastRead);
    }

    private Reading duplicateCheck(Reading reading) {
        Reading cleanReading = reading;
        ElecReading curElecReading;
        for (int i = 0; i < reading.getElecReadings().size(); i++) {
            curElecReading = reading.getElecReadings().get(i);
            for (int j = i + 1; j < reading.getElecReadings().size(); j++) {
                ElecReading elecReadingToProcess = reading.getElecReadings().get(j);
                if (curElecReading.getMeterId().equals(elecReadingToProcess.getMeterId())) {
                    if (curElecReading.getDate().compareTo(elecReadingToProcess.getDate()) == 0) {
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
                if (curGasReading.getMeterId().equals(gasReadingToProcess.getMeterId())) {
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
