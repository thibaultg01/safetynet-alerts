package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordRepository {
    List<MedicalRecord> findAll();
    MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);
}