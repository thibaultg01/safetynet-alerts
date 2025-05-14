package com.safetynet.alerts.repository;

import java.util.List;

public interface FirestationRepository {
    List<String> getAddressesByStationNumber(int stationNumber);
    int findStationNumberByAddress(String address);
    void addMapping(String address, int station);
    void updateMapping(String address, int newStation);
    void deleteMapping(String address, Integer station);
}