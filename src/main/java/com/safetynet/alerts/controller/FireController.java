package com.safetynet.alerts.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.service.FireService;

/**
 * Contrôleur REST permettant de récupérer les informations sur les personnes
 * vivant à une adresse donnée, ainsi que le numéro de la caserne de pompiers
 * qui les dessert.
 *
 * Ce contrôleur gère l’endpoint GET /fire
 *
 * Exemple d’URL : http://localhost:8080/fire?address=1509 Culver St
 *
 * Le résultat comprend : - Le numéro de la caserne desservant l’adresse - Une
 * liste des habitants avec leur nom, téléphone, âge, antécédents médicaux
 * (médicaments, allergies)
 */
@RestController
public class FireController {

	private static final Logger logger = LogManager.getLogger(FireController.class);

	private final FireService fireService;

	public FireController(FireService fireService) {
		this.fireService = fireService;
	}

	/**
	 * Récupère la liste des habitants à une adresse spécifique, ainsi que le numéro
	 * de caserne les desservant. Si l'adresse n'existe pas dans les données,
	 * retourne un objet JSON vide (`{}`).
	 *
	 * @param address l’adresse pour laquelle on souhaite récupérer les informations
	 * @return un {@link ResponseEntity} contenant un objet {@link FireResponseDTO}
	 *         ou un JSON vide
	 */
	@GetMapping("/fire")
	public ResponseEntity<?> getFireInfoByAddress(@RequestParam String address) {
		if (logger.isDebugEnabled()) {
			logger.debug("GET /fire appelé avec l'adresse : {}", address);
		}

		FireResponseDTO response = fireService.getFireInfoByAddress(address);

		if (response == null) {
			logger.info("Aucune donnée trouvée pour l'adresse '{}'. Réponse vide envoyée.", address);
			return ResponseEntity.ok().body(java.util.Collections.emptyMap());
		}

		logger.info("Réponse générée : {} résident(s), station(s) = {}", response.getResidents().size(),
				response.getStation());

		return ResponseEntity.ok(response);
	}
}