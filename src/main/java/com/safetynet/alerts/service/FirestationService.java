package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;

public interface FirestationService {
    FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber);
    List<String> getPhoneNumbersByFirestation(int stationNumber);
    void addMapping(String address, int station);
    void updateMapping(String address, int newStation);
    void deleteMapping(String address, Integer station);
}
