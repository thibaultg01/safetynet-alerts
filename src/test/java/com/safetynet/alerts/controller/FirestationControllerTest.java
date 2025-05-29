package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonCoveredByStationDTO;
import com.safetynet.alerts.model.Firestation;
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

import java.util.List;

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
    void testGetPersonsCoveredByStation_Success() throws Exception {
        int stationNumber = 3;

        // Préparation des données simulées
        PersonCoveredByStationDTO personDTO = new PersonCoveredByStationDTO();
        personDTO.setFirstName("John");
        personDTO.setLastName("Doe");
        personDTO.setAddress("1509 Culver St");
        personDTO.setPhone("841-874-6512");

        FirestationCoverageResponseDTO responseDTO = new FirestationCoverageResponseDTO();
        responseDTO.setPersons(List.of(personDTO));
        responseDTO.setNumberOfAdults(1);
        responseDTO.setNumberOfChildren(0);

        // Simulation du service
        when(firestationService.getPersonsCoveredByStation(stationNumber)).thenReturn(responseDTO);

        // Appel MockMvc
        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.persons[0].firstName").value("John"))
                .andExpect(jsonPath("$.numberOfAdults").value(1))
                .andExpect(jsonPath("$.numberOfChildren").value(0));

        verify(firestationService, times(1)).getPersonsCoveredByStation(stationNumber);
    }
    
    @Test
    void testGetPersonsCoveredByStation_NoAddressesFound() throws Exception {
        int stationNumber = 99;

        when(firestationService.getPersonsCoveredByStation(stationNumber))
                .thenThrow(new IllegalArgumentException("Adresse(s) introuvable pour la station n°99"));

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Adresse(s) introuvable pour la station n°99"));

        verify(firestationService, times(1)).getPersonsCoveredByStation(stationNumber);
    }

    @Test
    void testAddMapping_Success() throws Exception {
        doNothing().when(firestationService).addMapping(address, station);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Firestation(address, station))))
                .andExpect(status().isCreated());

        verify(firestationService, times(1)).addMapping(address, station);
    }

    @Test
    void testAddMapping_Conflict() throws Exception {
        doThrow(new IllegalArgumentException("L'attribution de cette adresse existe déjà."))
                .when(firestationService).addMapping(address, station);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Firestation(address, station))))
                .andExpect(status().isConflict());

        verify(firestationService, times(1)).addMapping(address, station);
    }

    @Test
    void testUpdateMapping_Success() throws Exception {
        doNothing().when(firestationService).updateMapping(address, station);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Firestation(address, station))))
                .andExpect(status().isOk());

        verify(firestationService, times(1)).updateMapping(address, station);
    }

    @Test
    void testUpdateMapping_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("introuvable"))
                .when(firestationService).updateMapping(address, station);

        mockMvc.perform(put("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Firestation(address, station))))
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
        doThrow(new IllegalArgumentException("introuvable"))
                .when(firestationService).deleteMapping(address, station);

        mockMvc.perform(delete("/firestation")
                        .param("address", address)
                        .param("station", String.valueOf(station)))
                .andExpect(status().isNotFound());

        verify(firestationService, times(1)).deleteMapping(address, station);
    }
}