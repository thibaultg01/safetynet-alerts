package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new PersonDTO();
        sampleDto.setFirstName("John");
        sampleDto.setLastName("Doe");
        sampleDto.setAddress("123 rue");
        sampleDto.setCity("Paris");
        sampleDto.setZip("75000");
        sampleDto.setPhone("0123456789");
        sampleDto.setEmail("john@example.com");
    }

    @Test
    void testAddPerson_Success() throws Exception {
        when(personService.addPerson(any())).thenReturn(sampleDto);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"));

        verify(personService).addPerson(any());
    }

    @Test
    void testAddPerson_Conflict() throws Exception {
        when(personService.addPerson(any())).thenThrow(new IllegalArgumentException("La personne existe déjà."));

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isConflict())
                .andExpect(content().string("La personne existe déjà."));
    }

    @Test
    void testUpdatePerson_Success() throws Exception {
        when(personService.updatePerson(any())).thenReturn(sampleDto);

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(personService).updatePerson(any());
    }

    @Test
    void testUpdatePerson_NotFound() throws Exception {
        when(personService.updatePerson(any())).thenThrow(new IllegalArgumentException("Personne introuvable."));

        mockMvc.perform(put("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleDto)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Personne introuvable."));
    }

    @Test
    void testDeletePerson_Success() throws Exception {
        doNothing().when(personService).deletePerson("John", "Doe");

        mockMvc.perform(delete("/person")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().isNoContent());

        verify(personService).deletePerson("John", "Doe");
    }

    @Test
    void testDeletePerson_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Personne introuvable."))
                .when(personService).deletePerson("Ghost", "User");

        mockMvc.perform(delete("/person")
                        .param("firstName", "Ghost")
                        .param("lastName", "User"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Personne introuvable."));
    }
}