package com.safetynet.alerts.repository;

import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordRepository {
	List<MedicalRecord> findAll();

	MedicalRecord findByFirstNameAndLastName(String firstName, String lastName);

	/**
	 * Recherche un dossier médical par prénom et nom.
	 * 
	 * @param firstName prénom
	 * @param lastName  nom
	 * @return un Optional contenant le dossier trouvé ou vide
	 */
	Optional<MedicalRecord> findByName(String firstName, String lastName);

	/**
	 * Ajoute un nouveau dossier médical.
	 * 
	 * @param medicalRecord le dossier à ajouter
	 * @return le dossier ajouté
	 */
	MedicalRecord save(MedicalRecord medicalRecord);

	/**
	 * Met à jour un dossier médical existant.
	 * 
	 * @param medicalRecordDTO données à mettre à jour
	 * @return le dossier mis à jour
	 */
	MedicalRecord update(MedicalRecordDTO medicalRecordDTO);

	/**
	 * Supprime un dossier médical par nom et prénom.
	 * 
	 * @param firstName prénom
	 * @param lastName  nom
	 * @return true si le dossier a été supprimé, false sinon
	 */
	boolean delete(String firstName, String lastName);
}