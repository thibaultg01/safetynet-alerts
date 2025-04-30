package com.safetynet.alerts.service;

import java.util.List;

public interface CommunityEmailService {
    List<String> getEmailsByCity(String city);
}