package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.PersonServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    private PersonRepository personRepository;
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        personRepository = mock(PersonRepository.class);
        personService = new PersonServiceImpl(personRepository);
    }

    @Test
    void testAddPerson_Success() {
        PersonDTO dto = createSampleDto();
        when(personRepository.findByName("John", "Doe")).thenReturn(Optional.empty());

        PersonDTO result = personService.addPerson(dto);

        assertEquals(dto.getFirstName(), result.getFirstName());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void testAddPerson_AlreadyExists() {
        PersonDTO dto = createSampleDto();
        when(personRepository.findByName("John", "Doe")).thenReturn(Optional.of(new Person()));

        assertThrows(IllegalArgumentException.class, () -> personService.addPerson(dto));
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void testUpdatePerson_Success() {
        PersonDTO dto = createSampleDto();
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");

        when(personRepository.findByName("John", "Doe")).thenReturn(Optional.of(person));

        PersonDTO result = personService.updatePerson(dto);

        assertEquals(dto.getAddress(), person.getAddress());
        verify(personRepository, times(1)).update(person);
    }

    @Test
    void testUpdatePerson_NotFound() {
        PersonDTO dto = createSampleDto();
        when(personRepository.findByName("John", "Doe")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> personService.updatePerson(dto));
        verify(personRepository, never()).update(any(Person.class));
    }

    @Test
    void testDeletePerson_Success() {
        doNothing().when(personRepository).deleteByName("John", "Doe");

        assertDoesNotThrow(() -> personService.deletePerson("John", "Doe"));
        verify(personRepository, times(1)).deleteByName("John", "Doe");
    }

    private PersonDTO createSampleDto() {
        PersonDTO dto = new PersonDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 Main St");
        dto.setCity("Culver");
        dto.setZip("97451");
        dto.setPhone("000-000-0000");
        dto.setEmail("john@example.com");
        return dto;
    }
}
