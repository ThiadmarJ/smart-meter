package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.GasReading;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class InitialiseRepositoryService {

    @Autowired
    private ReadingRepository readingRepository;

    public void initialiseRepository(int numRecords, int numInnerRecords) {
        readingRepository.deleteAll();
        for (int i = 0; i < numRecords; i++) {
            Reading reading = new Reading((long) i);
            List<GasReading> gasReadings = new ArrayList<>();
            List<ElecReading> elecReadings = new ArrayList<>();
            for (int j = 0; j < numInnerRecords; j++) {
                gasReadings.add(generateGasReading(reading));
                elecReadings.add(generateElecReading(reading));
            }
            reading.setGasReadings(gasReadings);
            reading.setElecReadings(elecReadings);
            readingRepository.save(reading);
        }
    }

    private GasReading generateGasReading(Reading reading) {
        return new GasReading(someLong(),someLong(),someLong(),Date.from(Instant.now()),reading);
    }
    private ElecReading generateElecReading(Reading reading) {
        return new ElecReading(someLong(),someLong(),someLong(), Date.from(Instant.now()),reading);
    }
    private Long someLong() {
        return new Random().nextLong(0,100);
    }
}
