package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Set;

public interface FirestationRepository {
    List<String> getAddressesByStationNumber(int stationNumber);
    Set<String> findAddressesByStationNumbers(List<Integer> stationNumbers);
    int findStationNumberByAddress(String address);
    void addMapping(String address, int station);
    void updateMapping(String address, int newStation);
    void deleteMapping(String address, Integer station);
}