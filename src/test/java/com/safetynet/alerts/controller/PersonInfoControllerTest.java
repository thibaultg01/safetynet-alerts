package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PersonInfoControllerTest {

	private MockMvc mockMvc;

	private PersonInfoService personInfoService;

	@BeforeEach
	void setup() {
		personInfoService = mock(PersonInfoService.class);
		PersonInfoController controller = new PersonInfoController(personInfoService);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	void getPersonInfoByLastName_shouldReturnPersonInfoDTOList() throws Exception {
		when(personInfoService.getPersonsByLastName("Doe")).thenReturn(List.of(new PersonInfoDTO()));

		mockMvc.perform(get("/personInfo").param("lastName", "Doe").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));

		verify(personInfoService).getPersonsByLastName("Doe");
	}
}
