package com.safetynet.alerts.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;
import com.safetynet.alerts.service.FirestationService;

@RestController
public class FirestationController {

	private static final Logger logger = LoggerFactory.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/firestation")
    public ResponseEntity<FirestationCoverageResponseDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        logger.info("Requête reçue : GET /firestation?stationNumber={}", stationNumber);

        FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

        if (response.getPersons().isEmpty()) {
            logger.warn("Aucune personne trouvée pour la caserne n°{}", stationNumber);
            return ResponseEntity.noContent().build(); // ou ResponseEntity.ok avec une liste vide si tu préfères
        }

        logger.info("Réponse préparée pour la caserne n°{} : {} personnes", stationNumber, response.getPersons().size());
        return ResponseEntity.ok(response);
    }
}