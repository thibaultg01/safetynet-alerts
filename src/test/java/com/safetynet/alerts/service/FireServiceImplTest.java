package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.FireServiceImpl;
import com.safetynet.alerts.utils.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireServiceImplTest {

	private PersonRepository personRepository;
	private MedicalRecordRepository medicalRecordRepository;
	private FirestationRepository firestationRepository;
	private FireServiceImpl fireService;

	@BeforeEach
	void setUp() {
		personRepository = mock(PersonRepository.class);
		medicalRecordRepository = mock(MedicalRecordRepository.class);
		firestationRepository = mock(FirestationRepository.class);
		fireService = new FireServiceImpl(personRepository, medicalRecordRepository, firestationRepository);
	}

	@Test
	void testGetPersonsAndStationByAddress() {
		String address = "1509 Culver St";

		Person person = new Person("John", "Boyd", address, "Culver", "97451", "841-874-6512", "john@example.com");
		MedicalRecord record = new MedicalRecord("John", "Boyd", "06/03/1985", List.of("aznol:350mg"),
				List.of("peanut"));

		when(personRepository.findByAddress(address)).thenReturn(List.of(person));
		when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(record);
		when(firestationRepository.findStationNumberByAddress(address)).thenReturn(3);

		FireResponseDTO response = fireService.getFireInfoByAddress(address);

		assertEquals(3, response.getStation());
		assertEquals(1, response.getResidents().size());

		FirePersonDTO resident = response.getResidents().get(0);
		assertEquals("John", resident.getFirstName());
		assertEquals("Boyd", resident.getLastName());
		assertEquals("841-874-6512", resident.getPhone());
		assertEquals(DateUtils.calculateAge("06/03/1985"), resident.getAge());
		assertEquals(List.of("aznol:350mg"), resident.getMedications());
		assertEquals(List.of("peanut"), resident.getAllergies());
	}
}