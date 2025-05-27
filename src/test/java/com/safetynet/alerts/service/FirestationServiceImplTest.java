package com.safetynet.alerts.service;

import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.FirestationServiceImpl;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private FirestationRepository firestationRepository;

    @InjectMocks
    private FirestationServiceImpl firestationService;

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