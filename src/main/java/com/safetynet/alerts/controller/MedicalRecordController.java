package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.MedicalRecordDTO;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des dossiers médicaux. Gère les opérations
 * d'ajout, de mise à jour et de suppression.
 */
@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

	private static final Logger logger = LogManager.getLogger(MedicalRecordController.class);
	private final MedicalRecordService medicalRecordService;

	public MedicalRecordController(MedicalRecordService medicalRecordService) {
		this.medicalRecordService = medicalRecordService;
	}

	/**
	 * Ajoute un nouveau dossier médical.
	 *
	 * @param dto le dossier médical à ajouter
	 * @return le dossier ajouté ou une erreur
	 */
	@PostMapping
	public ResponseEntity<MedicalRecordDTO> addMedicalRecord(@RequestBody MedicalRecordDTO dto) {
		if (logger.isDebugEnabled()) {
			logger.debug("Requête POST /medicalRecord reçue avec : {}", dto);
		}

		MedicalRecordDTO created = medicalRecordService.addMedicalRecord(dto);

		logger.info("Dossier médical ajouté avec succès : {} {}", created.getFirstName(), created.getLastName());
		return ResponseEntity.ok(created);
	}

	/**
	 * Met à jour un dossier médical existant.
	 *
	 * @param dto le dossier médical à mettre à jour
	 * @return le dossier mis à jour ou une erreur
	 */
	@PutMapping
	public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@RequestBody MedicalRecordDTO dto) {
		if (logger.isDebugEnabled()) {
			logger.debug("Réception de la requête PUT /medicalRecord avec : {}", dto);
		}

		MedicalRecordDTO updated = medicalRecordService.updateMedicalRecord(dto);
		
		logger.info("Dossier médical mis à jour avec succès : {} {}", updated.getFirstName(), updated.getLastName());
		return ResponseEntity.ok(updated);
	}

	/**
	 * Supprime un dossier médical.
	 *
	 * @param firstName prénom de la personne
	 * @param lastName  nom de la personne
	 * @return code 200 si supprimé, 404 sinon
	 */
	@DeleteMapping
	public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Réception de la requête DELETE /medicalRecord avec : {} {}", firstName, lastName);
		}

		medicalRecordService.deleteMedicalRecord(firstName, lastName);
		logger.info("Dossier médical supprimé avec succès : {} {}", firstName, lastName);
		return ResponseEntity.ok().build();
	}
}
