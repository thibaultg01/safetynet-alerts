package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.service.ChildAlertService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
public class ChildAlertController {

    private static final Logger logger = LoggerFactory.getLogger(ChildAlertController.class);

    private final ChildAlertService childAlertService;

    public ChildAlertController(ChildAlertService childAlertService) {
        this.childAlertService = childAlertService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildAlert(@RequestParam String address) {
        logger.info("Appel du endpoint /childAlert avec l'adresse : {}", address);
        List<ChildAlertDTO> children = childAlertService.getChildrenByAddress(address);

        if (children.isEmpty()) {
            logger.info("Aucun enfant trouvé à l'adresse : {}", address);
            return ResponseEntity.ok(Collections.emptyList());
        }

        logger.info("Réponse envoyée avec {} enfants", children.size());
        return ResponseEntity.ok(children);
    }
}