package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        logger.info("GET /communityEmail called with city={}", city);
        List<String> emails = communityEmailService.getEmailsByCity(city);
        logger.info("Found {} email(s) for city '{}'", emails.size(), city);
        return emails;
    }
}