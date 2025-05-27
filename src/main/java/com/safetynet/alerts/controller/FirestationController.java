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

@RestController
public class FirestationController {

	private static final Logger logger = LogManager.getLogger(FirestationController.class);

    private final FirestationService firestationService;

    public FirestationController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/firestation")
    public ResponseEntity<FirestationCoverageResponseDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        logger.debug("Requête reçue : GET /firestation?stationNumber={}", stationNumber);

        FirestationCoverageResponseDTO response = firestationService.getPersonsCoveredByStation(stationNumber);

        if (response.getPersons().isEmpty()) {
            logger.warn("Aucune personne trouvée pour la caserne n°{}", stationNumber);
            return ResponseEntity.noContent().build();
        }

        logger.info("Réponse préparée pour la caserne n°{} : {} personnes", stationNumber, response.getPersons().size());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/firestation")
    public ResponseEntity<Void> addMapping(@RequestBody Firestation firestation) {
        logger.info("POST /firestation - Ajout mapping : {}", firestation);
        firestationService.addMapping(firestation.getAddress(), firestation.getStation());
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/firestation")
    public ResponseEntity<Void> updateMapping(@RequestBody Firestation firestation) {
        logger.info("PUT /firestation - Mise à jour mapping : {}", firestation);
        firestationService.updateMapping(firestation.getAddress(), firestation.getStation());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/firestation")
    public ResponseEntity<Void> deleteMapping(@RequestParam(required = false) String address,
                                              @RequestParam(required = false) Integer station) {
        logger.info("DELETE /firestation - Suppression mapping address='{}' station='{}'", address, station);
        firestationService.deleteMapping(address, station);
        return ResponseEntity.noContent().build();
    }
}
