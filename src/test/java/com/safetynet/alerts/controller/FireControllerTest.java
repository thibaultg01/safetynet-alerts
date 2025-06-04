package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.service.FireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FireController.class)
class FireControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FireService fireService;

	@Test
	void testGetFireInfoByAddress_ReturnsOk() throws Exception {
		FirePersonDTO personDTO = new FirePersonDTO("John", "Boyd", "841-874-6512", 40, List.of("aznol:350mg"),
				List.of("peanut"));
		FireResponseDTO responseDTO = new FireResponseDTO(3, List.of(personDTO));

		when(fireService.getFireInfoByAddress("1509 Culver St")).thenReturn(responseDTO);

		mockMvc.perform(get("/fire").param("address", "1509 Culver St")).andExpect(status().isOk())
				.andExpect(jsonPath("$.station").value(3)).andExpect(jsonPath("$.residents[0].firstName").value("John"))
				.andExpect(jsonPath("$.residents[0].allergies[0]").value("peanut"));
	}

	@Test
	void testGetFireInfoByAddress_EmptyList() throws Exception {
		// GIVEN
		String unknownAddress = "999 nullpart";
		when(fireService.getFireInfoByAddress(unknownAddress)).thenReturn(null);

		// WHEN + THEN
		mockMvc.perform(get("/fire").param("address", unknownAddress).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json("{}"));
	}
}