package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LogManager.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
    	if (logger.isDebugEnabled()) {
            logger.debug("GET /communityEmail called with city={}", city);
    	}
        List<String> emails = communityEmailService.getEmailsByCity(city);
        logger.info("Found {} email(s) for city '{}'", emails.size(), city);
        return emails;
    }
}