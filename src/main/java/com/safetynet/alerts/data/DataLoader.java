package com.safetynet.alerts.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.model.MedicalRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Component
public class DataLoader {

    private List<Person> persons;
    private List<Firestation> firestations;
    private List<MedicalRecord> medicalRecords;

    public List<Person> getPersons() {
        return persons;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    @PostConstruct
    public void init() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("data.json").getInputStream();

            Map<String, Object> fullData = mapper.readValue(inputStream, new TypeReference<>() {});

            persons = mapper.convertValue(fullData.get("persons"), new TypeReference<List<Person>>() {});
            firestations = mapper.convertValue(fullData.get("firestations"), new TypeReference<List<Firestation>>() {});
            medicalRecords = mapper.convertValue(fullData.get("medicalrecords"), new TypeReference<List<MedicalRecord>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chargement de data.json", e);
        }
    }
}