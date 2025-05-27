package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    private String address;
    private int station;

    @BeforeEach
    void setUp() {
        address = "1509 Culver St";
        station = 3;
    }

    @Test
    void testAddMapping_Success() throws Exception {
        doNothing().when(firestationService).addMapping(address, station);

        mockMvc.perform(post("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isCreated());

        verify(firestationService, times(1)).addMapping(address, station);
    }

    @Test
    void testAddMapping_Conflict() throws Exception {
        doThrow(new IllegalStateException("L'attribution de cette adresse existe déjà."))
                .when(firestationService).addMapping(address, station);

        mockMvc.perform(post("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isConflict());

        verify(firestationService, times(1)).addMapping(address, station);
    }

    @Test
    void testUpdateMapping_Success() throws Exception {
        doNothing().when(firestationService).updateMapping(address, station);

        mockMvc.perform(put("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isOk());

        verify(firestationService, times(1)).updateMapping(address, station);
    }

    @Test
    void testUpdateMapping_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Not found"))
                .when(firestationService).updateMapping(address, station);

        mockMvc.perform(put("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isNotFound());

        verify(firestationService, times(1)).updateMapping(address, station);
    }

    @Test
    void testDeleteMapping_Success() throws Exception {
        doNothing().when(firestationService).deleteMapping(address, station);

        mockMvc.perform(delete("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isNoContent());

        verify(firestationService, times(1)).deleteMapping(address, station);
    }

    @Test
    void testDeleteMapping_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Not found"))
                .when(firestationService).deleteMapping(address, station);

        mockMvc.perform(delete("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isNotFound());

        verify(firestationService, times(1)).deleteMapping(address, station);
    }
}