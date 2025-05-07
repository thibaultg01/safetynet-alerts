package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonPersonRepository implements PersonRepository {

	private final DataLoader dataLoader;

    public JsonPersonRepository(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public List<Person> findAll(){
        return dataLoader.getPersons();
    }

    @Override
    public Optional<Person> findByName(String firstName, String lastName){
        return dataLoader.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
    @Override
    public List<Person> findByLastName(String lastName) {
        return dataLoader.getPersons().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return dataLoader.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                          && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Person> findByAddress(String address) {
        return dataLoader.getPersons().stream()
                .filter(p -> p.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());
    }
}