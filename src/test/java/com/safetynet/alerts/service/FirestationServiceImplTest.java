package com.safetynet.alerts.service;

import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.FirestationServiceImpl;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceImplTest {

	@Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FirestationServiceImpl firestationService;

    @Test
    void testGetPersonsCoveredByStation_Success() {
        int stationNumber = 3;

        // Données simulées
        String address = "1509 Culver St";

        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress(address);
        person.setPhone("841-874-6512");

        MedicalRecord record = new MedicalRecord();
        record.setFirstName("John");
        record.setLastName("Doe");
        record.setBirthdate("03/06/1984"); // > 18 ans

        // Mocks
        when(firestationRepository.getAddressesByStationNumber(stationNumber)).thenReturn(List.of(address));
        when(personRepository.findAll()).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(record);

        // Appel
        FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

        // Vérifs
        assertEquals(1, response.getPersons().size());
        assertEquals(1, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
        assertEquals("John", response.getPersons().get(0).getFirstName());
    }
    
    @Test
    void testGetPersonsCoveredByStation_NoAddresses() {
        int stationNumber = 99;

        when(firestationRepository.getAddressesByStationNumber(stationNumber)).thenReturn(Collections.emptyList());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                firestationService.getPersonsCoveredByStation(stationNumber));

        assertEquals("Adresse(s) introuvable pour la station n°99", ex.getMessage());
    }
    
    @Test
    void testGetPersonsCoveredByStation_NoPersonsMatched() {
        int stationNumber = 2;
        String address = "29 15th St";

        // L'adresse existe mais aucune personne n'y vit
        when(firestationRepository.getAddressesByStationNumber(stationNumber)).thenReturn(List.of(address));
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

        assertEquals(0, response.getPersons().size());
        assertEquals(0, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
    }
    
    @Test
    void testGetPersonsCoveredByStation_PersonWithoutMedicalRecord() {
        int stationNumber = 3;
        String address = "1509 Culver St";

        Person person = new Person();
        person.setFirstName("Jane");
        person.setLastName("Smith");
        person.setAddress(address);
        person.setPhone("123-456-7890");

        when(firestationRepository.getAddressesByStationNumber(stationNumber)).thenReturn(List.of(address));
        when(personRepository.findAll()).thenReturn(List.of(person));
        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Smith")).thenReturn(null);

        FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

        assertEquals(0, response.getPersons().size());
        assertEquals(0, response.getNumberOfAdults());
        assertEquals(0, response.getNumberOfChildren());
    }
    
    @Test
    public void getPhoneNumbersByFirestation_shouldReturnCorrectPhones() {
        // Given
        int stationNumber = 1;
        List<String> addresses = List.of("1509 Culver St", "29 15th St");

        List<Person> people = List.of(
            new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841-874-6512", "john.boyd@example.com"),
            new Person("Jacob", "Boyd", "29 15th St", "Culver", "97451", "841-874-7458", "jacob.boyd@example.com"),
            new Person("Tenley", "Boyd", "892 Downing Ct", "Culver", "97451", "841-874-1234", "tenley.boyd@example.com") // not in zone
        );

        Mockito.when(firestationRepository.getAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        Mockito.when(personRepository.findAll()).thenReturn(people);

        // When
        List<String> result = firestationService.getPhoneNumbersByFirestation(stationNumber);

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.contains("841-874-6512"));
        Assertions.assertTrue(result.contains("841-874-7458"));
        Assertions.assertFalse(result.contains("841-874-1234"));
    }
    
    @Test
    void testAddMapping_Success() {
        when(firestationRepository.addMapping("123 Main St", 1)).thenReturn(true);
        assertDoesNotThrow(() -> firestationService.addMapping("123 Main St", 1));
        verify(firestationRepository).addMapping("123 Main St", 1);
    }

    @Test
    void testAddMapping_AlreadyExists() {
        when(firestationRepository.addMapping("123 Main St", 1)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                firestationService.addMapping("123 Main St", 1));
        assertEquals("L'attribution de cette adresse existe déjà.", exception.getMessage());
        verify(firestationRepository).addMapping("123 Main St", 1);
    }

    @Test
    void testUpdateMapping_Success() {
        when(firestationRepository.updateMapping("123 Main St", 2)).thenReturn(true);
        assertDoesNotThrow(() -> firestationService.updateMapping("123 Main St", 2));
        verify(firestationRepository).updateMapping("123 Main St", 2);
    }

    @Test
    void testUpdateMapping_NotFound() {
        when(firestationRepository.updateMapping("Unknown St", 2)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                firestationService.updateMapping("Unknown St", 2));
        assertEquals("Adresse introuvable.", exception.getMessage());
        verify(firestationRepository).updateMapping("Unknown St", 2);
    }

    @Test
    void testDeleteMapping_Success() {
        when(firestationRepository.deleteMapping("123 Main St", 1)).thenReturn(true);
        assertDoesNotThrow(() -> firestationService.deleteMapping("123 Main St", 1));
        verify(firestationRepository).deleteMapping("123 Main St", 1);
    }

    @Test
    void testDeleteMapping_NotFound() {
        when(firestationRepository.deleteMapping("Unknown St", 2)).thenReturn(false);
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                firestationService.deleteMapping("Unknown St", 2));
        assertEquals("Caserne introuvable.", exception.getMessage());
        verify(firestationRepository).deleteMapping("Unknown St", 2);
    }
}