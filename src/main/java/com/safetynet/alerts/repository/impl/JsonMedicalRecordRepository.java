package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.dto.MedicalRecordDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonMedicalRecordRepository implements MedicalRecordRepository {

	private static final Logger logger = LogManager.getLogger(JsonMedicalRecordRepository.class);

	private final DataLoader dataLoader;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final String DATA_FILE_PATH = "src/main/resources/data.json";

	public JsonMedicalRecordRepository(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	@Override
	public List<MedicalRecord> findAll() {
		return dataLoader.getMedicalRecords();
	}

	@Override
	public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
		return dataLoader.getMedicalRecords().stream().filter(
				mr -> mr.getFirstName().equalsIgnoreCase(firstName) && mr.getLastName().equalsIgnoreCase(lastName))
				.findFirst().orElse(null);
	}

	@Override
	public Optional<MedicalRecord> findByName(String firstName, String lastName) {
		return dataLoader.getMedicalRecords().stream()
				.filter(r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName().equalsIgnoreCase(lastName))
				.findFirst();
	}

	@Override
	public MedicalRecord save(MedicalRecord record) {
		if (logger.isDebugEnabled()) {
			logger.debug("Ajout du dossier médical en mémoire : {} {}", record.getFirstName(), record.getLastName());
		}

		dataLoader.getMedicalRecords().add(record);
		writeToFile();

		logger.info("Dossier médical enregistré avec succès : {} {}", record.getFirstName(), record.getLastName());
		return record;
	}

	@Override
	public MedicalRecord update(MedicalRecordDTO dto) {

		if (logger.isDebugEnabled()) {
			logger.debug("Mise à jour du dossier médical : {} {}", dto.getFirstName(), dto.getLastName());
		}
		MedicalRecord record = findByName(dto.getFirstName(), dto.getLastName()).get();
		record.setBirthdate(dto.getBirthdate());
		record.setMedications(dto.getMedications());
		record.setAllergies(dto.getAllergies());

		writeToFile();
		logger.info("Dossier médical mis à jour avec succès : {} {}", dto.getFirstName(), dto.getLastName());
		return record;
	}

	@Override
	public boolean delete(String firstName, String lastName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Suppression du dossier médical : {} {}", firstName, lastName);
		}

		boolean removed = dataLoader.getMedicalRecords().removeIf(
				r -> r.getFirstName().equalsIgnoreCase(firstName) && r.getLastName().equalsIgnoreCase(lastName));

		if (removed) {
			writeToFile();
			logger.info("Dossier médical supprimé avec succès : {} {}", firstName, lastName);
		} else {
			logger.error("Échec de la suppression : dossier médical introuvable pour {} {}", firstName, lastName);
		}

		return removed;
	}

	public void writeToFile() {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), dataLoader);
			if (logger.isDebugEnabled()) {
				logger.debug("Fichier dataTest.json mis à jour.");
			}
		} catch (IOException e) {
			logger.error("Erreur lors de la sauvegarde du fichier JSON : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
		}
	}
}