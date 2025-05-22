package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private MedicalRecordService medicalRecordService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MedicalRecordDTO sampleDto;

    @BeforeEach
    public void setup() {
        sampleDto = new MedicalRecordDTO();
        sampleDto.setFirstName("John");
        sampleDto.setLastName("Doe");
        sampleDto.setBirthdate("01/01/1990");
        sampleDto.setMedications(Collections.singletonList("doliprane:500mg"));
        sampleDto.setAllergies(Collections.singletonList("pollen"));
    }

    @Test
    void testAddMedicalRecord_Success() throws Exception {
        Mockito.when(medicalRecordService.addMedicalRecord(any())).thenReturn(sampleDto);

        mockMvc.perform(post("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testAddMedicalRecord_Conflict() throws Exception {
        Mockito.when(medicalRecordService.addMedicalRecord(any()))
                .thenThrow(new IllegalArgumentException("Le dossier médical existe déjà."));

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Le dossier médical existe déjà."));
    }

    @Test
    void testUpdateMedicalRecord_Success() throws Exception {
        Mockito.when(medicalRecordService.updateMedicalRecord(any())).thenReturn(sampleDto);

        mockMvc.perform(put("/medicalRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.birthdate").value("01/01/1990"));
    }

    @Test
    void testUpdateMedicalRecord_NotFound() throws Exception {
        Mockito.when(medicalRecordService.updateMedicalRecord(any()))
                .thenThrow(new IllegalArgumentException("Dossier médical introuvable."));

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical introuvable."));
    }

    @Test
    void testDeleteMedicalRecord_Success() throws Exception {
        Mockito.when(medicalRecordService.deleteMedicalRecord(eq("John"), eq("Doe"))).thenReturn(true);

        mockMvc.perform(delete("/medicalRecord")
                .param("firstName", "John")
                .param("lastName", "Doe"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteMedicalRecord_NotFound() throws Exception {
        Mockito.when(medicalRecordService.deleteMedicalRecord("Jane", "Smith"))
                .thenThrow(new IllegalArgumentException("Dossier médical introuvable."));

        mockMvc.perform(delete("/medicalRecord")
                        .param("firstName", "Jane")
                        .param("lastName", "Smith"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dossier médical introuvable."));
    }
}