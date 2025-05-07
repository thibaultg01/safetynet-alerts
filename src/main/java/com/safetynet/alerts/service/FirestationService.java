package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;

public interface FirestationService {
    FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber);
}
