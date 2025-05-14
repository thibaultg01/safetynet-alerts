package com.safetynet.alerts.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

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
}