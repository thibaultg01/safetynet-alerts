package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST permettant de récupérer les adresses email de toutes les personnes
 * vivant dans une ville donnée.
 *
 */
@RestController
public class CommunityEmailController {

    private static final Logger logger = LogManager.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    /**
     * Endpoint GET pour obtenir la liste des adresses email des habitants d'une ville.
     * Les emails sont retournés sous forme de liste JSON. Si aucun email n’est trouvé,
     * la réponse est une liste vide.
     *
     * @param city le nom de la ville pour laquelle on souhaite obtenir les adresses email
     * @return une liste d'adresses email correspondant à la ville
     */
    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
		if (logger.isDebugEnabled()) {
			logger.debug("GET /communityEmail appelé avec la ville ={}", city);
		}
        List<String> emails = communityEmailService.getEmailsByCity(city);
        logger.info(" {} email(s) trouvé pour la ville '{}'", emails.size(), city);
        return emails;
    }
}