package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommunityEmailController {

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {
        return communityEmailService.getEmailsByCity(city);
    }
}