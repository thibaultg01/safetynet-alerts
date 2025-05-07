package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JsonFirestationRepository implements FirestationRepository {

    private final DataLoader dataLoader;

    public JsonFirestationRepository(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    @Override
    public List<String> getAddressesByStationNumber(int stationNumber) {
        return dataLoader.getFirestations().stream()
                .filter(fs -> fs.getStation() == stationNumber)
                .map(Firestation::getAddress)
                .collect(Collectors.toList());
    }
}
