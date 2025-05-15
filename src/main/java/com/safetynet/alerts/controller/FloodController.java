package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.service.FloodService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FloodController {

	private final FloodService floodService;
	private static final Logger logger = LogManager.getLogger(FloodController.class);

	public FloodController(FloodService floodService) {
		this.floodService = floodService;
	}

	@GetMapping("/flood/stations")
	public ResponseEntity<List<FloodAddressDTO>> getHouseholdsByStations(@RequestParam List<Integer> stations) {
		logger.info("Requete reçue pour les stations : {}", stations);
		try {
			List<FloodAddressDTO> result = floodService.getHouseholdsByStations(stations);
			if (logger.isDebugEnabled()) {
			logger.debug("Resultat obtenu : {}", result);
			}
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error("Erreur lors du traitement de la requête pour les stations : {}", stations, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
