package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.impl.JsonMedicalRecordRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonMedicalRecordRepositoryTest {

    private JsonMedicalRecordRepository repository;
    private DataLoader dataLoaderMock;
    private ObjectMapper objectMapper = new ObjectMapper();

    private List<MedicalRecord> medicalRecords;

    @BeforeEach
    void setUp() {
        // Données fictives
        medicalRecords = new ArrayList<>();
        medicalRecords.add(new MedicalRecord("John", "Doe", "01/01/1980", List.of("med1"), List.of("allergy1")));

        dataLoaderMock = mock(DataLoader.class);
        when(dataLoaderMock.getMedicalRecords()).thenReturn(medicalRecords);

        repository = new JsonMedicalRecordRepository(dataLoaderMock);
    }

    @Test
    void findAll_shouldReturnAllMedicalRecords() {
        List<MedicalRecord> result = repository.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findByFirstNameAndLastName_shouldReturnCorrectRecord() {
        MedicalRecord record = repository.findByFirstNameAndLastName("John", "Doe");
        assertThat(record).isNotNull();
        assertThat(record.getAllergies()).contains("allergy1");
    }

    @Test
    void findByName_shouldReturnOptionalPresent() {
        Optional<MedicalRecord> result = repository.findByName("John", "Doe");
        assertThat(result).isPresent();
    }

    @Test
    void save_shouldAddRecordAndWriteToFile() {
        MedicalRecord newRecord = new MedicalRecord("Jane", "Smith", "02/02/1990", List.of("med2"), List.of());

        JsonMedicalRecordRepository repoSpy = spy(repository);
        doNothing().when(repoSpy).writeToFile();

        repoSpy.save(newRecord);

        assertThat(medicalRecords).hasSize(2);
        assertThat(medicalRecords).anySatisfy(r -> {
            assertThat(r.getFirstName()).isEqualTo("Jane");
            assertThat(r.getMedications()).contains("med2");
        });

        verify(repoSpy).writeToFile();
    }

    @Test
    void update_shouldModifyRecordAndWriteToFile() {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setBirthdate("05/05/1985");
        dto.setMedications(List.of("updatedMed"));
        dto.setAllergies(List.of("updatedAllergy"));

        JsonMedicalRecordRepository repoSpy = spy(repository);
        doNothing().when(repoSpy).writeToFile();

        MedicalRecord updated = repoSpy.update(dto);

        assertThat(updated.getBirthdate()).isEqualTo("05/05/1985");
        assertThat(updated.getMedications()).contains("updatedMed");
        verify(repoSpy).writeToFile();
    }

    @Test
    void update_shouldThrowIfRecordNotFound() {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setFirstName("Ghost");
        dto.setLastName("User");

        assertThatThrownBy(() -> repository.update(dto))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void delete_shouldRemoveRecordAndWriteToFile() {
        JsonMedicalRecordRepository repoSpy = spy(repository);
        doNothing().when(repoSpy).writeToFile();

        boolean result = repoSpy.delete("John", "Doe");

        assertThat(result).isTrue();
        assertThat(medicalRecords).isEmpty();
        verify(repoSpy).writeToFile();
    }

    @Test
    void delete_shouldReturnFalseIfRecordNotFound() {
        boolean result = repository.delete("Fake", "Person");
        assertThat(result).isFalse();
    }
}