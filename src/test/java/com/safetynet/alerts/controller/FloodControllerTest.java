package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.service.FloodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FloodController.class)
class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FloodService floodService;

    @Test
    void testGetHouseholdsByStations_ReturnsResults() throws Exception {
    	FloodAddressDTO household1 = new FloodAddressDTO("1509 Culver St", List.of());
    	FloodAddressDTO household2 = new FloodAddressDTO("29 15th St", List.of());

        when(floodService.getHouseholdsByStations(List.of(1, 2)))
                .thenReturn(List.of(household1, household2));

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "1,2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("1509 Culver St"))
                .andExpect(jsonPath("$[1].address").value("29 15th St"));
    }

    @Test
    void testGetHouseholdsByStations_EmptyResult() throws Exception {
        when(floodService.getHouseholdsByStations(List.of(99)))
                .thenReturn(List.of());

        mockMvc.perform(get("/flood/stations")
                        .param("stations", "99"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}
