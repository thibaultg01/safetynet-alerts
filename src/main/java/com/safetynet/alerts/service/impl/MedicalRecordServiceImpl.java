package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implémentation du service métier pour la gestion des dossiers médicaux.
 * Gère les opérations d'ajout, de mise à jour et de suppression.
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private static final Logger logger = LogManager.getLogger(MedicalRecordServiceImpl.class);
    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecordServiceImpl(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    /** {@inheritDoc} */
    @Override
    public MedicalRecordDTO addMedicalRecord(MedicalRecordDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("Vérification de l'existence du dossier médical à ajouter.");
        }
        if (medicalRecordRepository.findByName(dto.getFirstName(), dto.getLastName()).isPresent()) {
            logger.error("Le dossier médical {} {} existe déjà.", dto.getFirstName(), dto.getLastName());
            throw new IllegalArgumentException("Le dossier médical existe déjà.");
        }

        medicalRecordRepository.save(convertToEntity(dto));
        logger.info("Dossier médical ajouté avec succès : {} {}", dto.getFirstName(), dto.getLastName());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("Recherche du dossier médical à mettre à jour.");
        }

        Optional<MedicalRecord> existing = medicalRecordRepository.findByName(dto.getFirstName(), dto.getLastName());
        if (existing.isEmpty()) {
            logger.error("Dossier médical introuvable pour la mise à jour : {} {}", dto.getFirstName(), dto.getLastName());
            throw new IllegalArgumentException("Dossier médical introuvable.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Mise à jour des champs du dossier médical.");
        }

        medicalRecordRepository.update(dto);
        logger.info("Dossier médical mis à jour avec succès : {} {}", dto.getFirstName(), dto.getLastName());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Suppression du dossier médical : {} {}", firstName, lastName);
        }

        boolean deleted = medicalRecordRepository.delete(firstName, lastName);
        if (deleted) {
            logger.info("Suppression réussie du dossier médical : {} {}", firstName, lastName);
        } else {
            logger.error("Échec de la suppression : dossier médical introuvable pour {} {}", firstName, lastName);
            throw new IllegalArgumentException("Dossier médical introuvable.");
        }
        return deleted;
    }

    private MedicalRecord convertToEntity(MedicalRecordDTO dto) {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName(dto.getFirstName());
        record.setLastName(dto.getLastName());
        record.setBirthdate(dto.getBirthdate());
        record.setMedications(dto.getMedications());
        record.setAllergies(dto.getAllergies());
        return record;
    }
}