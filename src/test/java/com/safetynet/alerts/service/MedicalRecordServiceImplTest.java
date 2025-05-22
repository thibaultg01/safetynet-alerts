package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.impl.MedicalRecordServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordServiceImplTest {

	private MedicalRecordRepository repository;
	private MedicalRecordServiceImpl service;

	@BeforeEach
	void setup() {
		repository = mock(MedicalRecordRepository.class);
		service = new MedicalRecordServiceImpl(repository);
	}

	@Test
	void testAddMedicalRecord_Success() {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setFirstName("John");
		dto.setLastName("Doe");
		dto.setBirthdate("01/01/1990");
		dto.setMedications(Collections.emptyList());
		dto.setAllergies(Collections.emptyList());
		when(repository.findByName("John", "Doe")).thenReturn(Optional.empty());
		when(repository.save(any())).thenReturn(new MedicalRecord(dto));

		MedicalRecordDTO result = service.addMedicalRecord(dto);

		assertNotNull(result);
		assertEquals("John", result.getFirstName());
		verify(repository).save(any(MedicalRecord.class));
	}

	@Test
	void testAddMedicalRecord_AlreadyExists() {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setFirstName("John");
		dto.setLastName("Doe");
		dto.setBirthdate("01/01/1990");
		dto.setMedications(Collections.emptyList());
		dto.setAllergies(Collections.emptyList());

		when(repository.findByName("John", "Doe")).thenReturn(Optional.of(new MedicalRecord()));

		assertThrows(IllegalArgumentException.class, () -> {
			service.addMedicalRecord(dto);
		});

		verify(repository, never()).save(any());
	}

	@Test
	void testUpdateMedicalRecord_Success() {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setFirstName("John");
		dto.setLastName("Doe");
		dto.setBirthdate("01/01/1990");
		dto.setMedications(Collections.emptyList());
		dto.setAllergies(Collections.emptyList());
		when(repository.findByName("John", "Doe")).thenReturn(Optional.of(new MedicalRecord(dto)));
		when(repository.update(dto)).thenReturn(new MedicalRecord(dto));

		MedicalRecordDTO result = service.updateMedicalRecord(dto);

		assertNotNull(result);
		assertEquals("01/01/1990", result.getBirthdate());
		verify(repository).update(dto);
	}

	@Test
	void testUpdateMedicalRecord_NotFound() {
		MedicalRecordDTO dto = new MedicalRecordDTO();
		dto.setFirstName("Ghost");
		dto.setLastName("User");
		dto.setBirthdate("01/01/1990");
		dto.setMedications(Collections.emptyList());
		dto.setAllergies(Collections.emptyList());

		when(repository.findByName("Ghost", "User")).thenReturn(Optional.empty());

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			service.updateMedicalRecord(dto);
		});

		assertEquals("Dossier médical introuvable.", exception.getMessage());
		verify(repository, never()).update(any());
	}

	@Test
	void testDeleteMedicalRecord_Success() {
		when(repository.delete("John", "Doe")).thenReturn(true);

		boolean result = service.deleteMedicalRecord("John", "Doe");

		assertTrue(result);
		verify(repository).delete("John", "Doe");
	}

	@Test
	void testDeleteMedicalRecord_NotFound() {
		when(repository.delete("Jane", "Smith")).thenReturn(false);

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			service.deleteMedicalRecord("Jane", "Smith");
		});

		assertEquals("Dossier médical introuvable.", exception.getMessage());
		verify(repository).delete("Jane", "Smith");
	}
}