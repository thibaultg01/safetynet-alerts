package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.data.DataLoader;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JsonMedicalRecordRepository implements MedicalRecordRepository {

    private final List<MedicalRecord> medicalRecords;

    public JsonMedicalRecordRepository(DataLoader dataLoader) {
        this.medicalRecords = dataLoader.getMedicalRecords();
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecords;
    }

    @Override
    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(mr -> mr.getFirstName().equalsIgnoreCase(firstName)
                           && mr.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
}