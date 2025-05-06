package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    List<Person> findAll() throws IOException;
    Optional<Person> findByName(String firstName, String lastName) throws IOException;
    List<Person> findByLastName(String lastName);
    Person findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByAddress(String address);
}