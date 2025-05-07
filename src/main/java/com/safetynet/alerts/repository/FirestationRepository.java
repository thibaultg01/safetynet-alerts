package com.safetynet.alerts.repository;

import java.util.List;

public interface FirestationRepository {
    List<String> getAddressesByStationNumber(int stationNumber);
}