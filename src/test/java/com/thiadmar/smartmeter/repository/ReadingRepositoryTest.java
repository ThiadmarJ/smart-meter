package com.thiadmar.smartmeter.repository;

import com.thiadmar.smartmeter.model.ElecReading;
import com.thiadmar.smartmeter.model.GasReading;
import com.thiadmar.smartmeter.model.Reading;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
class ReadingRepositoryTest {

    @Autowired
    private ReadingRepository readingRepository;

    private static final Reading reading = new Reading(123L);
    private static final Date testDate = Date.from(Instant.EPOCH);
    private static final GasReading gasReading = new GasReading(1L, 2L, 3L, testDate, reading);
    private static final ElecReading elecReading = new ElecReading(1L, 2L, 3L, testDate, reading);
    private static final List<GasReading> gasReadings = Arrays.asList(gasReading);
    private static final List<ElecReading> elecReadings = Arrays.asList(elecReading);

    @Test
    void should_create_new_reading() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);

        Optional<Reading> readingById = readingRepository.findById(123L);
        assertThat(readingById.isPresent());
    }

    @Test
    void should_find_all_readings() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);

        Iterable<Reading> readings = readingRepository.findAll();
        assertThat(readings).hasSize(1);
    }

    @Test
    void should_delete_readings() {
        reading.setGasReadings(gasReadings);
        reading.setElecReadings(elecReadings);
        readingRepository.save(reading);
        readingRepository.delete(reading);

        Iterable<Reading> readings = readingRepository.findAll();
        assertThat(readings).hasSize(0);
    }

}