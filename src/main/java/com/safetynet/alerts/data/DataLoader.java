package com.safetynet.alerts.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader {

    private ObjectMapper objectMapper = new ObjectMapper();
    private String jsonFilePath;

    private List<Person> persons = new ArrayList<>();
    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalRecords = new ArrayList<>();
    /**
     * Constructeur par défaut pour Spring
     */
    public DataLoader() {
        // jsonFilePath reste null, Spring utilisera init()
    }

    @PostConstruct
    public void init() {
            try (InputStream inputStream = new ClassPathResource("data.json").getInputStream()) {
                Map<String, Object> fullData = objectMapper.readValue(inputStream, new TypeReference<>() {});
                loadListsFromMap(fullData);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors du chargement de data.json depuis le classpath", e);
            }
    }

    private void loadListsFromMap(Map<String, Object> fullData) {
        this.persons = objectMapper.convertValue(fullData.getOrDefault("persons", new ArrayList<>()), new TypeReference<>() {});
        this.firestations = objectMapper.convertValue(fullData.getOrDefault("firestations", new ArrayList<>()), new TypeReference<>() {});
        this.medicalRecords = objectMapper.convertValue(fullData.getOrDefault("medicalRecords", new ArrayList<>()), new TypeReference<>() {});
    }

    // === Getters ===

    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }
    
    
}