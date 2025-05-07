package com.safetynet.alerts.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.safetynet.alerts.service.FirestationService;


@RestController
public class PhoneAlertController {

    private static final Logger logger = LogManager.getLogger(PhoneAlertController.class);

    private final FirestationService firestationService;

    public PhoneAlertController(FirestationService firestationService) {
        this.firestationService = firestationService;
    }

    @GetMapping("/phoneAlert")
    public List<String> getPhonesByFirestation(@RequestParam("firestation") int stationNumber) {
        logger.info("Requête reçue : récupération des numéros de téléphone pour la caserne n°{}", stationNumber);

        List<String> phoneNumbers = firestationService.getPhoneNumbersByFirestation(stationNumber);

        if (logger.isDebugEnabled()) {
            logger.debug("Numéros de téléphone récupérés pour la caserne {} : {}", stationNumber, phoneNumbers);
        }

        logger.info("Traitement terminé pour /phoneAlert avec la caserne n°{}", stationNumber);

        return phoneNumbers;
    }
}
