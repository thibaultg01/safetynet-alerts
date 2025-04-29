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

@Repository
public class JsonPersonRepository implements PersonRepository {

    private List<Person> readPersonsFromFile() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = new ClassPathResource("data.json").getInputStream();
        JsonNode rootNode = objectMapper.readTree(inputStream);
        return Arrays.asList(objectMapper.treeToValue(rootNode.get("persons"), Person[].class));
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
}