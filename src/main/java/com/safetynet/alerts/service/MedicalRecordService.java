package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.MedicalRecordDTO;

/**
 * Service métier pour la gestion des dossiers médicaux. Gère les opérations
 * d'ajout, de mise à jour et de suppression.
 * 
 * Un dossier médical est identifié par le couple (prénom, nom).
 */
public interface MedicalRecordService {

	/**
	 * Ajoute un nouveau dossier médical.
	 * 
	 * @param dto les informations du dossier médical à ajouter
	 * @return le dossier médical ajouté, ou null si un dossier existe déjà (cas
	 *         loggué)
	 */
	MedicalRecordDTO addMedicalRecord(MedicalRecordDTO dto);

	/**
	 * Met à jour un dossier médical existant. Le prénom et le nom servent
	 * d'identifiants uniques et ne sont pas modifiables.
	 * 
	 * @param dto les nouvelles données à appliquer au dossier existant
	 * @return le dossier mis à jour, ou null si aucun dossier n'est trouvé (cas
	 *         loggué)
	 */
	MedicalRecordDTO updateMedicalRecord(MedicalRecordDTO dto);

	/**
	 * Supprime un dossier médical en fonction du prénom et du nom.
	 * 
	 * @param firstName le prénom de la personne
	 * @param lastName  le nom de la personne
	 * @return true si la suppression a réussi, false sinon (cas loggué)
	 */
	boolean deleteMedicalRecord(String firstName, String lastName);
}