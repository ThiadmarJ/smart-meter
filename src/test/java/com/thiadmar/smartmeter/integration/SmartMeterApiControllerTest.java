package com.thiadmar.smartmeter.integration;

import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.model.ReadingResponse;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import com.thiadmar.smartmeter.service.SmartMeterApiService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SmartMeterApiControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private ReadingRepository readingRepository;

    private static final Long ACCOUNT_ID = 100L;
    private static final String USERNAME = "user";
    private static final String PASSWORD = "password";
    private static final String URL_PATH = "/api/smart/reads/";

    @Test
    public void should_store_then_return_reading() {
        Reading reading = new Reading(ACCOUNT_ID);
        template.withBasicAuth(USERNAME,PASSWORD)
                .postForEntity(URL_PATH, reading, ReadingResponse.class);
        ResponseEntity<Reading> result = template.withBasicAuth(USERNAME, PASSWORD)
                .getForEntity(URL_PATH + ACCOUNT_ID, Reading.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ACCOUNT_ID, result.getBody().getAccountId());
    }

    @Test
    public void should_return_not_found_when_user_not_found() {
        ResponseEntity<Reading> result = template.withBasicAuth(USERNAME, PASSWORD)
                .getForEntity(URL_PATH + 10, Reading.class);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    public void should_return_unauthorised_when_wrong_credentials() {
        ResponseEntity<Reading> result = template.withBasicAuth("wrong", "wrong")
                .getForEntity(URL_PATH + ACCOUNT_ID, Reading.class);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    public void should_accept_reading() {
        Reading reading = new Reading(ACCOUNT_ID);
        ResponseEntity<ReadingResponse> result =  template.withBasicAuth(USERNAME,PASSWORD)
                .postForEntity(URL_PATH, reading, ReadingResponse.class);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(ACCOUNT_ID, result.getBody().getId());
    }

}
