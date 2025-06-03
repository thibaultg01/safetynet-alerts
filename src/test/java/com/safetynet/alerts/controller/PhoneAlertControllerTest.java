package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhoneAlertController.class)
class PhoneAlertControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private FirestationService firestationService;

	@Test
	void testGetPhoneAlertByStationNumber_ReturnsPhoneList() throws Exception {
		List<String> phones = List.of("123-456-7890", "987-654-3210");

		when(firestationService.getPhoneNumbersByFirestation(1)).thenReturn(phones);

		mockMvc.perform(get("/phoneAlert").param("firestation", "1")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0]").value("123-456-7890")).andExpect(jsonPath("$[1]").value("987-654-3210"));
	}

	@Test
	void testGetPhoneAlertByStationNumber_EmptyResult() throws Exception {
		when(firestationService.getPhoneNumbersByFirestation(99)).thenReturn(List.of());

		mockMvc.perform(get("/phoneAlert").param("firestation", "99")).andExpect(status().isOk())
				.andExpect(content().string("[]"));
	}

	@Test
	void testGetPhoneAlertByStationNumber_MissingParameter() throws Exception {
		mockMvc.perform(get("/phoneAlert")).andExpect(status().isBadRequest());
	}
}