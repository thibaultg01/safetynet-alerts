package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    private final PersonRepository personRepository;

    public DataService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getAllPersons() throws IOException {
        return personRepository.findAll();
    }

    public Optional<Person> getPersonByName(String firstName, String lastName) throws IOException {
        return personRepository.findByName(firstName, lastName);
    }
}