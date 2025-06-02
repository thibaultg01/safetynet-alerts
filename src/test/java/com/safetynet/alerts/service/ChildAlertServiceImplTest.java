package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.ChildAlertServiceImpl;
import com.safetynet.alerts.utils.DateUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChildAlertServiceImplTest {

    private PersonRepository personRepository;
    private MedicalRecordRepository medicalRecordRepository;
    private ChildAlertServiceImpl childAlertService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        medicalRecordRepository = mock(MedicalRecordRepository.class);
        childAlertService = new ChildAlertServiceImpl(personRepository, medicalRecordRepository);
    }

    @Test
    void testGetChildrenByAddress_WithChildrenAndAdults() {
        String address = "1509 Culver St";

        Person child = new Person("John", "Boyd", address, "Culver", "97451", "841-874-6512", "john@example.com");
        Person adult = new Person("Jane", "Boyd", address, "Culver", "97451", "841-874-6512", "jane@example.com");

        String childBirthDate = "01/01/2015";
        MedicalRecord childRecord = new MedicalRecord("John", "Boyd", "01/01/2015", List.of("aznol:350mg"), List.of("peanut"));
        MedicalRecord adultRecord = new MedicalRecord("Jane", "Boyd", "03/01/1980", List.of(), List.of());

        when(personRepository.findByAddress(address)).thenReturn(List.of(child, adult));
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Boyd")).thenReturn(childRecord);
        when(medicalRecordRepository.findByFirstNameAndLastName("Jane", "Boyd")).thenReturn(adultRecord);

        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress(address);

        assertEquals(1, result.size());
        ChildAlertDTO childAlertDTO = result.get(0);
        assertEquals("John", childAlertDTO.getFirstName());
        assertEquals("Boyd", childAlertDTO.getLastName());

        int expectedAge = DateUtils.calculateAge(childBirthDate);
        assertEquals(expectedAge, childAlertDTO.getAge());

        assertEquals(1, childAlertDTO.getHouseholdMembers().size());
    }

    @Test
    void testGetChildrenByAddress_NoChildren() {
        String address = "Unknown Street";
        when(personRepository.findByAddress(address)).thenReturn(List.of());

        List<ChildAlertDTO> result = childAlertService.getChildrenByAddress(address);
        assertTrue(result.isEmpty());
    }
}
