package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;

public interface FirestationService {
    FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber);
    List<String> getPhoneNumbersByFirestation(int stationNumber);
}
