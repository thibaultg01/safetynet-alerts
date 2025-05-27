package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Set;

public interface FirestationRepository {
    List<String> getAddressesByStationNumber(int stationNumber);
    Set<String> findAddressesByStationNumbers(List<Integer> stationNumbers);
    int findStationNumberByAddress(String address);
    boolean addMapping(String address, int station);
    boolean updateMapping(String address, int newStation);
    boolean deleteMapping(String address, Integer station);
}