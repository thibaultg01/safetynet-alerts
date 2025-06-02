package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.service.ChildAlertService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChildAlertService childAlertService;

    @Test
    void testGetChildrenByAddress_ReturnsChildren() throws Exception {
        ChildAlertDTO child = new ChildAlertDTO("John", "Boyd", 9, List.of());
        when(childAlertService.getChildrenByAddress("1509 Culver St")).thenReturn(List.of(child));

        mockMvc.perform(get("/childAlert")
                        .param("address", "1509 Culver St"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].age").value(9));
    }

    @Test
    void testGetChildrenByAddress_NoChildren() throws Exception {
        when(childAlertService.getChildrenByAddress("unknown")).thenReturn(List.of());

        mockMvc.perform(get("/childAlert")
                        .param("address", "unknown"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}
