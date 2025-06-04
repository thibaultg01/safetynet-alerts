package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.FloodServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FloodServiceImplTest {

	private FloodServiceImpl floodService;
	private FirestationRepository firestationRepository;
	private PersonRepository personRepository;
	private MedicalRecordRepository medicalRecordRepository;

	@BeforeEach
	void setUp() {
		firestationRepository = mock(FirestationRepository.class);
		personRepository = mock(PersonRepository.class);
		medicalRecordRepository = mock(MedicalRecordRepository.class);

		floodService = new FloodServiceImpl(firestationRepository, personRepository, medicalRecordRepository);
	}

	@Test
	void testGetHouseholdsByStations_returnsExpectedData() {
		// Given
		List<Integer> stations = Arrays.asList(1, 2);
		Set<String> addresses = new HashSet<>(Arrays.asList("1509 Culver St"));
		when(firestationRepository.findAddressesByStationNumbers(stations)).thenReturn(addresses);

		Person person = new Person();
		person.setFirstName("John");
		person.setLastName("Boyd");
		person.setPhone("841-874-6512");
		person.setAddress("1509 Culver St");

		List<Person> personList = List.of(person);
		when(personRepository.findByAddress("1509 Culver St")).thenReturn(personList);

		MedicalRecord record = new MedicalRecord();
		record.setFirstName("John");
		record.setLastName("Boyd");
		LocalDate birthDate = LocalDate.now().minusYears(38);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		record.setBirthdate(birthDate.format(formatter));
		record.setMedications(List.of("aznol:350mg"));
		record.setAllergies(List.of("nillacilan"));

		when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(record);

		// When
		List<FloodAddressDTO> result = floodService.getHouseholdsByStations(stations);

		// Then
		assertEquals(1, result.size());
		FloodAddressDTO dto = result.get(0);
		assertEquals("1509 Culver St", dto.getAddress());
		assertEquals(1, dto.getResidents().size());
		assertEquals("John", dto.getResidents().get(0).getFirstName());
		assertEquals("Boyd", dto.getResidents().get(0).getLastName());
		assertEquals("841-874-6512", dto.getResidents().get(0).getPhone());
		assertEquals(38, dto.getResidents().get(0).getAge());
		assertEquals(List.of("aznol:350mg"), dto.getResidents().get(0).getMedications());
		assertEquals(List.of("nillacilan"), dto.getResidents().get(0).getAllergies());
	}
}
