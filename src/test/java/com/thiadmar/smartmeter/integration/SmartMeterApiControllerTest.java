package com.thiadmar.smartmeter.integration;

import com.thiadmar.smartmeter.exceptions.ControllerExceptionHandler;
import com.thiadmar.smartmeter.model.Reading;
import com.thiadmar.smartmeter.repository.ReadingRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class SmartMeterApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReadingRepository readingRepository;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.
                standaloneSetup(new SmartMeterApiController())
                .setControllerAdvice(new ControllerExceptionHandler()).build();
    }

    @Test
    public void should_return_ok_and_expected_body_after_get_request() throws Exception {
        Reading reading = new Reading(100L);
        when(readingRepository.findById(100L)).thenReturn(Optional.of(reading));

        mvc.perform(get("/api/smart/reads/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{accountId:100,gasReadings:null,elecReadings:null}"));
    }

    @Test
    public void should_throw_exception_when_user_not_found() throws Exception {
        when(readingRepository.findById(100L)).thenReturn(Optional.empty());
        mvc.perform(get("/api/smart/reads/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}