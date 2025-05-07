package com.safetynet.alerts.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.service.FireService;


/**
 * Controleur REST permettant de récupérer les informations sur les personnes vivant à une adresse donnée,
 * ainsi que le numéro de la caserne de pompiers qui les dessert.
 * 
 * Ce controleur gère l’endpoint {@code GET /fire}.
 *
 * Exemple d’URL : {@code http://localhost:8080/fire?address=1509 Culver St}
 *
 * Le résultat comprend :
 * 
 *   - Le numero de la caserne desservant l’adresse
 *   - Une liste des habitants avec leur nom, téléphone, age, antécédents médicaux (médicaments, allergies)
 * 
 */

/**
 * Constructeur du controleur FireController.
 *
 * @param fireService le service métier utilisé pour récupérer les données liées à une adresse
 */
@RestController
public class FireController {

    private static final Logger logger = LogManager.getLogger(FireController.class);

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    /**
     * Récupère la liste des habitants à une adresse spécifique, ainsi que le numéro de caserne les desservant.
     *
     * @param address l’adresse pour laquelle on souhaite récupérer les informations
     * @return un objet {@link FireResponseDTO} contenant les informations des résidents et des casernes
     */
    @GetMapping("/fire")
    public FireResponseDTO getFireInfoByAddress(@RequestParam String address) {
        logger.info("GET /fire appelé avec l'adresse : {}", address);
        FireResponseDTO response = fireService.getFireInfoByAddress(address);
        logger.debug("Réponse générée : {} résident(s), station(s) = {}", 
                     response.getResidents().size(), response.getStation());
        return response;
    }
}
