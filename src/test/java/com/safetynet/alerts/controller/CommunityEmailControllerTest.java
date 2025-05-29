package com.safetynet.alerts.controller;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.service.CommunityEmailService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

@WebMvcTest(CommunityEmailController.class)
class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommunityEmailService communityEmailService;

    @Test
    void getEmails_shouldReturnEmailList() throws Exception {
        when(communityEmailService.getEmailsByCity("Bordeaux"))
                .thenReturn(Arrays.asList("test1@mail.com", "test2@mail.com"));

        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Bordeaux"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("test1@mail.com"))
                .andExpect(jsonPath("$[1]").value("test2@mail.com"));
    }

    @Test
    void getEmails_shouldReturn404_whenNoEmails() throws Exception {
        when(communityEmailService.getEmailsByCity("Inconnue"))
                .thenThrow(new ResourceNotFoundException("Aucune adresse email trouvée pour la ville : Inconnue"));

        mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                        .param("city", "Inconnue"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Aucune adresse email trouvée pour la ville : Inconnue"));
    }
}