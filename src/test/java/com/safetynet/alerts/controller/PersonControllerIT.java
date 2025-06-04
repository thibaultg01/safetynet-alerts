package com.safetynet.alerts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.dto.PersonDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonControllerIT {

	@BeforeEach
	public void cleanUp() {
		String url = "http://localhost:" + port + "/person?firstName=John&lastName=Myers";
		restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
	}

	@LocalServerPort // Injecte le port aléatoire sur lequel l'application tourne
	private int port;

	@Autowired
	private TestRestTemplate restTemplate; // Client HTTP simplifié fourni par Spring Boot pour tester les REST

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testCreatePerson() {
		// Arrange
		String url = "http://localhost:" + port + "/person";
		PersonDTO newPerson = new PersonDTO();
		newPerson.setFirstName("John");
		newPerson.setLastName("Myers");
		newPerson.setAddress("123 rue");
		newPerson.setCity("Paris");
		newPerson.setZip("75000");
		newPerson.setPhone("0123456789");
		newPerson.setEmail("john@example.com");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<PersonDTO> request = new HttpEntity<>(newPerson, headers);

		// Act
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

		// Assert
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).contains("John", "Myers");
	}
}