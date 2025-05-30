package com.safetynet.alerts.repository;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.impl.JsonPersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonPersonRepositoryTest {

    private DataLoader dataLoaderMock;
    private JsonPersonRepository repository;
    private List<Person> persons;

    @BeforeEach
    void setUp() {
        persons = new ArrayList<>();
        persons.add(new Person("John", "Doe", "123 Street", "City", "12345", "123-456-7890", "john@example.com"));

        dataLoaderMock = mock(DataLoader.class);
        when(dataLoaderMock.getPersons()).thenReturn(persons);

        repository = spy(new JsonPersonRepository(dataLoaderMock));
        doNothing().when(repository).saveToFile();
    }

    @Test
    void findAll_shouldReturnAllPersons() {
        List<Person> result = repository.findAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void findByName_shouldReturnPersonIfExists() {
        Optional<Person> result = repository.findByName("John", "Doe");
        assertThat(result).isPresent();
    }

    @Test
    void findByLastName_shouldReturnMatchingPersons() {
        List<Person> result = repository.findByLastName("Doe");
        assertThat(result).hasSize(1);
    }

    @Test
    void findByFirstNameAndLastName_shouldReturnPerson() {
        Person result = repository.findByFirstNameAndLastName("John", "Doe");
        assertThat(result).isNotNull();
    }

    @Test
    void findByAddress_shouldReturnMatchingPersons() {
        List<Person> result = repository.findByAddress("123 Street");
        assertThat(result).hasSize(1);
    }

    @Test
    void save_shouldAddPersonAndWriteToFile() {
        Person newPerson = new Person("Jane", "Smith", "456 Road", "Town", "54321", "987-654-3210", "jane@example.com");
        repository.save(newPerson);

        assertThat(persons).contains(newPerson);
        verify(repository).saveToFile();
        assertThat(persons).hasSize(2);
    }

    @Test
    void deleteByName_shouldRemovePersonAndWriteToFile() {
        boolean result = repository.deleteByName("John", "Doe");

        assertThat(result).isTrue();
        assertThat(persons).isEmpty();
        verify(repository).saveToFile();
    }

    @Test
    void deleteByName_shouldReturnFalseIfNotFound() {
        boolean result = repository.deleteByName("Jane", "Smith");
        assertThat(result).isFalse();
        verify(repository, never()).saveToFile();
    }

    @Test
    void update_shouldModifyPersonAndWriteToFile() {
        Person updated = new Person("John", "Doe", "New Address", "New City", "00000", "111-222-3333", "new@mail.com");
        repository.update(updated);

        Person result = persons.get(0);
        assertThat(result.getAddress()).isEqualTo("New Address");
        assertThat(result.getEmail()).isEqualTo("new@mail.com");
        verify(repository).saveToFile();
    }
}
