package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.PersonInfoServiceImpl;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonInfoServiceImplTest {

	private PersonRepository personRepository;
	private MedicalRecordRepository medicalRecordRepository;
	private PersonInfoServiceImpl personInfoService;

	@BeforeEach
	void setUp() {
		personRepository = mock(PersonRepository.class);
		medicalRecordRepository = mock(MedicalRecordRepository.class);
		personInfoService = new PersonInfoServiceImpl(personRepository, medicalRecordRepository);
	}

	@Test
	void testGetPersonsByLastName_ReturnsInfo() {
		String lastName = "Boyd";

		Person person = new Person("John", lastName, "1509 Culver St", "Culver", "97451", "841-874-6512",
				"john@example.com");
		MedicalRecord record = new MedicalRecord("John", lastName, "06/03/1985", List.of("aznol:350mg"),
				List.of("peanut"));

		when(personRepository.findByLastName(lastName)).thenReturn(List.of(person));
		when(medicalRecordRepository.findByFirstNameAndLastName("John", lastName)).thenReturn(record);

		List<PersonInfoDTO> result = personInfoService.getPersonsByLastName(lastName);

		assertEquals(1, result.size());

		PersonInfoDTO dto = result.get(0);
		assertEquals("John", dto.getFirstName());
		assertEquals("Boyd", dto.getLastName());
		assertEquals("1509 Culver St", dto.getAddress());
		assertEquals("john@example.com", dto.getEmail());
		assertEquals(DateUtils.calculateAge("06/03/1985"), dto.getAge());
		assertEquals(List.of("aznol:350mg"), dto.getMedications());
		assertEquals(List.of("peanut"), dto.getAllergies());
	}

	@Test
	void testGetPersonsByLastName_NoMedicalRecord() {
		String lastName = "Smith";

		Person person = new Person("Jane", lastName, "29 15th St", "Culver", "97451", "888-555-1234",
				"jane@example.com");

		when(personRepository.findByLastName(lastName)).thenReturn(List.of(person));
		when(medicalRecordRepository.findByFirstNameAndLastName("Jane", lastName)).thenReturn(null);

		List<PersonInfoDTO> result = personInfoService.getPersonsByLastName(lastName);

		assertEquals(1, result.size());
		PersonInfoDTO dto = result.get(0);

		assertEquals("Jane", dto.getFirstName());
		assertNull(dto.getMedications());
		assertNull(dto.getAllergies());
		assertEquals(0, dto.getAge());
	}

	@Test
	void testGetPersonsByLastName_EmptyResult() {
		when(personRepository.findByLastName("Unknown")).thenReturn(List.of());

		List<PersonInfoDTO> result = personInfoService.getPersonsByLastName("Unknown");

		assertTrue(result.isEmpty());
	}
}
