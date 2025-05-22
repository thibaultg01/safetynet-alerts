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

    private PersonRepository repository;
    private PersonServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(PersonRepository.class);
        service = new PersonServiceImpl(repository);
    }

    @Test
    void testAddPerson_Success() {
        PersonDTO dto = getSampleDTO();
        when(repository.findByName("John", "Doe")).thenReturn(Optional.empty());

        PersonDTO result = service.addPerson(dto);

        assertEquals(dto.getFirstName(), result.getFirstName());
        verify(repository).save(any());
    }

    @Test
    void testAddPerson_AlreadyExists() {
        PersonDTO dto = getSampleDTO();
        when(repository.findByName("John", "Doe")).thenReturn(Optional.of(new Person()));

        assertThrows(IllegalArgumentException.class, () -> service.addPerson(dto));
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdatePerson_Success() {
        PersonDTO dto = getSampleDTO();
        Person person = new Person();
        when(repository.findByName("John", "Doe")).thenReturn(Optional.of(person));

        PersonDTO result = service.updatePerson(dto);

        assertEquals(dto.getCity(), result.getCity());
        verify(repository).update(any());
    }

    @Test
    void testUpdatePerson_NotFound() {
        PersonDTO dto = getSampleDTO();
        when(repository.findByName("John", "Doe")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.updatePerson(dto));
        verify(repository, never()).update(any());
    }

    @Test
    void testDeletePerson_Success() {
        when(repository.deleteByName("John", "Doe")).thenReturn(true);

        assertDoesNotThrow(() -> service.deletePerson("John", "Doe"));
        verify(repository).deleteByName("John", "Doe");
    }

    @Test
    void testDeletePerson_NotFound() {
        when(repository.deleteByName("John", "Doe")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.deletePerson("John", "Doe"));
        verify(repository).deleteByName("John", "Doe");
    }

    private PersonDTO getSampleDTO() {
        PersonDTO dto = new PersonDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setAddress("123 Rue");
        dto.setCity("Paris");
        dto.setZip("75000");
        dto.setPhone("0102030405");
        dto.setEmail("john.doe@example.com");
        return dto;
    }
}
