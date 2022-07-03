package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.GasReading;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
                gasReadings.add(generateGasReading(reading, j));
                elecReadings.add(generateElecReading(reading, j));
            }
            reading.setGasReadings(gasReadings);
            reading.setElecReadings(elecReadings);
            readingRepository.save(reading);
        }
    }

    //Time dif is purely to keep each record distinct in Date given
    private GasReading generateGasReading(Reading reading, int timeDif) {
        return new GasReading(someLong(), someLong(), Date.from(Instant.now().minusMillis(timeDif)), reading);
    }

    private ElecReading generateElecReading(Reading reading, int timeDif) {
        return new ElecReading(someLong(), someLong(), Date.from(Instant.now().minusMillis(timeDif)), reading);
    }

    private Long someLong() {
        return new Random().nextLong(0, 100);
    }
}
