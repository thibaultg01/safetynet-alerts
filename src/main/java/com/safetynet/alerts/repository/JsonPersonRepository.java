package com.safetynet.alerts.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Person;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonPersonRepository implements PersonRepository {

	private List<Person> readPersonsFromFile() {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        InputStream inputStream = new ClassPathResource("data.json").getInputStream();
	        JsonNode rootNode = objectMapper.readTree(inputStream);
	        return Arrays.asList(objectMapper.treeToValue(rootNode.get("persons"), Person[].class));
	    } catch (IOException e) {
	        throw new RuntimeException("Erreur lors de la lecture de data.json", e);
	    }
	}

    @Override
    public List<Person> findAll() throws IOException {
        return readPersonsFromFile();
    }

    @Override
    public Optional<Person> findByName(String firstName, String lastName) throws IOException {
        return readPersonsFromFile().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                        && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst();
    }
    @Override
    public List<Person> findByLastName(String lastName) {
        return readPersonsFromFile().stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return readPersonsFromFile().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                          && p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
}