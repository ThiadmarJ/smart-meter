package com.thiadmar.smartmeter.service;

import com.thiadmar.smartmeter.model.reading.ElecReading;
import com.thiadmar.smartmeter.model.reading.GasReading;
import com.thiadmar.smartmeter.model.reading.Reading;
import com.thiadmar.smartmeter.model.response.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
class SmartMeterApiServiceTest {

    @Autowired
    private ReadingRepository readingRepository;

    private SmartMeterApiService smartMeterApiService;

    @BeforeEach
    void init() {
        smartMeterApiService = new SmartMeterApiService(readingRepository);
    }

    @Test
    void should_retrieve_reading() {
        Reading exampleReading = new Reading(11L);
        readingRepository.save(exampleReading);
        Reading reading = smartMeterApiService.retrieveReading(11L);
        Assertions.assertEquals(reading.getAccountId(), exampleReading.getAccountId());
    }

    @Test
    void should_receive_and_store_reading() {

        Reading exampleReading = new Reading(11L);
        ReadingResponse response = smartMeterApiService.receiveReading(exampleReading);
        Assertions.assertEquals(response.getAccountId(), exampleReading.getAccountId());
    }

    @Test
    void should_remove_duplicate() {
        Reading exampleReading = new Reading(11L);
        GasReading gasReading1 = new GasReading(1L, 2L, Date.from(Instant.EPOCH), exampleReading);
        GasReading gasReading2 = new GasReading(1L, 2L, Date.from(Instant.EPOCH), exampleReading);
        List<GasReading> gasReadings = List.of(gasReading1, gasReading2);
        exampleReading.setGasReadings(gasReadings);

        smartMeterApiService.receiveReading(exampleReading);
        Reading receivedReading = smartMeterApiService.retrieveReading(exampleReading.getAccountId());

        Assertions.assertEquals(receivedReading.getGasReadings().size(), 1);
    }

    @Test
    void should_store_multiple() {
        Reading exampleReading = new Reading(11L);
        GasReading gasReading1 = new GasReading(1L, 2L, Date.from(Instant.EPOCH), exampleReading);
        GasReading gasReading2 = new GasReading(1L, 2L, Date.from(Instant.now()), exampleReading);
        List<GasReading> gasReadings = List.of(gasReading1, gasReading2);
        exampleReading.setGasReadings(gasReadings);

        ElecReading elecReading1 = new ElecReading(1L, 2L, Date.from(Instant.EPOCH), exampleReading);
        ElecReading elecReading2 = new ElecReading(1L, 2L, Date.from(Instant.now()), exampleReading);
        List<ElecReading> elecReadings = List.of(elecReading1, elecReading2);
        exampleReading.setElecReadings(elecReadings);

        smartMeterApiService.receiveReading(exampleReading);
        Reading receivedReading = smartMeterApiService.retrieveReading(exampleReading.getAccountId());

        Assertions.assertEquals(receivedReading.getGasReadings().size(), 2);
        Assertions.assertEquals(receivedReading.getElecReadings().size(), 2);
    }

    @Test
    void should_return_correct_response_after_two_readings() {
        Reading exampleReading = new Reading(11L);
        GasReading gasReading = new GasReading(1L, 2L, Date.from(Instant.now().minusSeconds(86400)), exampleReading);
        List<GasReading> gasReadings = List.of(gasReading);
        exampleReading.setGasReadings(gasReadings);
        smartMeterApiService.receiveReading(exampleReading);

        Reading exampleReading2 = new Reading(11L);
        GasReading gasReading2 = new GasReading(1L, 20L, Date.from(Instant.now()), exampleReading2);
        List<GasReading> gasReadings2 = List.of(gasReading2);
        exampleReading2.setGasReadings(gasReadings2);
        ReadingResponse result = smartMeterApiService.receiveReading(exampleReading2);

        Long usage = result.getReadings().get(0).getUsageSinceLastRead();
        Long days = result.getReadings().get(0).getPeriodSinceLastRead();
        Assertions.assertEquals(18, usage);
        Assertions.assertEquals(1, days);
    }
}