package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.File;

@Repository
public class JsonFirestationRepository implements FirestationRepository {

    private final DataLoader dataLoader;
    
    private static final String DATA_FILE_PATH = "src/main/resources/dataTest.json";
    
    private final ObjectMapper objectMapper = new ObjectMapper();

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
    @Override
    public int findStationNumberByAddress(String address) {
        return dataLoader.getFirestations().stream()
            .filter(f -> f.getAddress().equalsIgnoreCase(address))
            .map(Firestation::getStation)
            .findFirst()
            .orElse(0);
        }
    @Override
    public Set<String> findAddressesByStationNumbers(List<Integer> stationNumbers) {
        return dataLoader.getFirestations().stream()
                .filter(f -> stationNumbers.contains(f.getStation()))
                .map(Firestation::getAddress)
                .collect(Collectors.toSet());
    }
    @Override
    public void addMapping(String address, int station) {
        dataLoader.getFirestations().add(new Firestation(address, station));
        saveToFile();
    }

    @Override
    public void updateMapping(String address, int newStation) {
        dataLoader.getFirestations().stream()
            .filter(f -> f.getAddress().equalsIgnoreCase(address))
            .findFirst()
            .ifPresent(f -> f.setStation(newStation));
        saveToFile();
    }

    @Override
    public void deleteMapping(String address, Integer station) {
        dataLoader.getFirestations().removeIf(f ->
            (address != null && f.getAddress().equalsIgnoreCase(address)) ||
            (station != null && f.getStation() == station));
        saveToFile();
    }
    
    private void saveToFile() {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), dataLoader);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
        }
    }
}
