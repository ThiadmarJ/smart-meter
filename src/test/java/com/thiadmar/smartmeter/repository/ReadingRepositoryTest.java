package com.thiadmar.smartmeter.repository;

import com.google.common.collect.Iterables;
import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.GasReading;
import com.thiadmar.smartmeter.model.Reading;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
class ReadingRepositoryTest {

    @Autowired
    private ReadingRepository readingRepository;

    private static final Reading reading = new Reading(123L);
    private static final Date testDate = Date.from(Instant.EPOCH);
    private static final GasReading gasReading = new GasReading(2L, 3L, testDate, reading);
    private static final ElecReading elecReading = new ElecReading(2L, 3L, testDate, reading);
    private static final List<GasReading> gasReadings = List.of(gasReading);
    private static final List<ElecReading> elecReadings = List.of(elecReading);

    @Test
    public void should_create_new_reading() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);

        Optional<Reading> readingById = readingRepository.findById(123L);
        Assertions.assertTrue(readingById.isPresent());
    }

    @Test
    void should_find_all_readings() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);

        Iterable<Reading> readings = readingRepository.findAll();
        Assertions.assertEquals(1, Iterables.size(readings));
    }

    @Test
    void can_not_find_reading() {
        Optional<Reading> readingById = readingRepository.findById(123L);
        Assertions.assertTrue(readingById.isEmpty());
    }

    @Test
    void should_delete_readings() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);
        readingRepository.deleteAll();

        Iterable<Reading> readings = readingRepository.findAll();
        Assertions.assertEquals(0, Iterables.size(readings));
    }

}