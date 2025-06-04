package com.safetynet.alerts.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.service.FirestationService;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour la gestion des mappings entre adresses et numéros de
 * casernes de pompiers. Permet d'ajouter, de modifier, de supprimer et de
 * consulter les personnes couvertes par une caserne.
 */
@RestController
public class FirestationController {

	private static final Logger logger = LogManager.getLogger(FirestationController.class);

	private final FirestationService firestationService;

	public FirestationController(FirestationService firestationService) {
		this.firestationService = firestationService;
	}

	/**
	 * Endpoint GET permettant d'obtenir la liste des personnes couvertes par une
	 * caserne donnée. Retourne également le nombre d'adultes et d'enfants.
	 *
	 * @param stationNumber le numéro de la caserne
	 * @return un objet {@link FirestationCoverageResponseDTO} contenant la liste
	 *         des personnes couvertes
	 */
	@GetMapping("/firestation")
	public ResponseEntity<FirestationCoverageResponseDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("Requête reçue : GET /firestation?stationNumber={}", stationNumber);
		}

		FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

		logger.info("Réponse préparée pour la caserne n°{} : {} personnes", stationNumber,
				response.getPersons().size());
		return ResponseEntity.ok(response);
	}

	/**
	 * Endpoint POST permettant d'ajouter un nouveau mapping adresse/station.
	 *
	 * @param firestation l'objet {@link Firestation} contenant l'adresse et le
	 *                    numéro de caserne à ajouter
	 * @return HTTP 201 si l'ajout a réussi
	 */
	@PostMapping("/firestation")
	public ResponseEntity<Void> addMapping(@RequestBody Firestation firestation) {
		if (logger.isDebugEnabled()) {
			logger.debug("POST /firestation - Ajout mapping : {}", firestation);
		}
		firestationService.addMapping(firestation.getAddress(), firestation.getStation());
		logger.info("Station ajoutée avec succès : {} {}", firestation.getAddress(), firestation.getStation());
		return ResponseEntity.status(201).build();
	}

	/**
	 * Endpoint PUT permettant de mettre à jour le numéro de caserne associé à une
	 * adresse.
	 *
	 * @param firestation l'objet {@link Firestation} contenant l'adresse et le
	 *                    nouveau numéro de caserne
	 * @return HTTP 200 si la mise à jour a réussi
	 */
	@PutMapping("/firestation")
	public ResponseEntity<Void> updateMapping(@RequestBody Firestation firestation) {
		if (logger.isDebugEnabled()) {
			logger.debug("PUT /firestation - Mise à jour mapping : {}", firestation);
		}
		firestationService.updateMapping(firestation.getAddress(), firestation.getStation());
		logger.info("Station mise à jour avec succès : {} {}", firestation.getAddress(), firestation.getStation());
		return ResponseEntity.ok().build();
	}

	/**
	 * Endpoint DELETE permettant de supprimer un mapping d'adresse ou de station.
	 * Les deux paramètres sont optionnels, mais au moins l’un des deux doit être
	 * fourni.
	 *
	 * @param address l’adresse à supprimer du mapping
	 * @param station le numéro de la caserne à supprimer
	 * @return HTTP 204 si la suppression a réussi
	 */
	@DeleteMapping("/firestation")
	public ResponseEntity<Void> deleteMapping(@RequestParam(required = false) String address,
			@RequestParam(required = false) Integer station) {
		if (logger.isDebugEnabled()) {
			logger.debug("DELETE /firestation - Suppression mapping address='{}' station='{}'", address, station);
		}
		firestationService.deleteMapping(address, station);
		logger.info("Station supprimée avec succès : {} {}", address, station);
		return ResponseEntity.noContent().build();
	}
}
