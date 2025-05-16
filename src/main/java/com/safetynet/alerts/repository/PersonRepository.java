package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    List<Person> findAll();
    Optional<Person> findByName(String firstName, String lastName);
    List<Person> findByLastName(String lastName);
    Person findByFirstNameAndLastName(String firstName, String lastName);
    List<Person> findByAddress(String address);
    void save(Person person);
    void deleteByName(String firstName, String lastName);
    void update(Person updatedPerson);
}