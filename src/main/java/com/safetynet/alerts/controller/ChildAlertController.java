package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.service.ChildAlertService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Contrôleur REST pour le endpoint childAlert.
 * Ce contrôleur permet de récupérer les enfants et les membres de leur foyer
 * à une adresse donnée.
 */

@RestController
public class ChildAlertController {

	private static final Logger logger = LogManager.getLogger(ChildAlertController.class);

	private final ChildAlertService childAlertService;

	public ChildAlertController(ChildAlertService childAlertService) {
		this.childAlertService = childAlertService;
	}

	/**
     * Endpoint REST permettant de récupérer les enfants vivant à une adresse donnée,
     * ainsi que les autres membres du foyer.
     *
     * @param address l'adresse ciblée
     * @return une liste de {@link ChildAlertDTO}
     */
	@GetMapping("/childAlert")
	public ResponseEntity<?> getChildAlert(@RequestParam String address) {
		if (logger.isDebugEnabled()) {
			logger.debug("Appel du endpoint /childAlert avec l'adresse : {}", address);
		}
		List<ChildAlertDTO> children = childAlertService.getChildrenByAddress(address);
		logger.info("Réponse envoyée avec {} enfants", children.size());
		return ResponseEntity.ok(children);
	}
}