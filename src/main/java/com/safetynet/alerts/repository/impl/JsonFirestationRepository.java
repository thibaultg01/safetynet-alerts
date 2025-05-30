package com.safetynet.alerts.repository.impl;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.FirestationRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.io.File;

@Repository
public class JsonFirestationRepository implements FirestationRepository {

	private final DataLoader dataLoader;

	private static final Logger logger = LogManager.getLogger(JsonPersonRepository.class);

	private static final String DATA_FILE_PATH = "src/main/resources/data.json";

	private final ObjectMapper objectMapper = new ObjectMapper();

	public JsonFirestationRepository(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	@Override
	public List<String> getAddressesByStationNumber(int stationNumber) {
		return dataLoader.getFirestations().stream().filter(fs -> fs.getStation() == stationNumber)
				.map(Firestation::getAddress).collect(Collectors.toList());
	}

	@Override
	public int findStationNumberByAddress(String address) {
		return dataLoader.getFirestations().stream().filter(f -> f.getAddress().equalsIgnoreCase(address))
				.map(Firestation::getStation).findFirst().orElse(0);
	}

	@Override
	public Set<String> findAddressesByStationNumbers(List<Integer> stationNumbers) {
		return dataLoader.getFirestations().stream().filter(f -> stationNumbers.contains(f.getStation()))
				.map(Firestation::getAddress).collect(Collectors.toSet());
	}

	@Override
	public boolean addMapping(String address, int station) {
		boolean alreadyExists = dataLoader.getFirestations().stream()
				.anyMatch(f -> f.getAddress().equalsIgnoreCase(address));
		if (alreadyExists) {
			logger.error("La caserne existe déjà à l'adresse : {}", address);
			return false;
		} else {
			dataLoader.getFirestations().add(new Firestation(address, station));
			saveToFile();
			logger.info("Caserne ajoutée avec succès : {} -> station {}", address, station);
			return true;
		}
	}

	@Override
	public boolean updateMapping(String address, int newStation) {
		Optional<Firestation> firestationOpt = dataLoader.getFirestations().stream()
				.filter(f -> f.getAddress().equalsIgnoreCase(address)).findFirst();

		if (firestationOpt.isPresent()) {
			firestationOpt.get().setStation(newStation);
			saveToFile();
			logger.info("Station mise à jour avec succès : {} -> {}", address, newStation);
			return true;
		} else {
			logger.error("Échec de la mise à jour : Station/adresse introuvable : {} {}", address, newStation);
			return false;
		}
	}

	@Override
	public boolean deleteMapping(String address, Integer station) {
		boolean removed = dataLoader.getFirestations()
				.removeIf(f -> (address != null && f.getAddress().equalsIgnoreCase(address))
						|| (station != null && f.getStation() == station));
		if (removed) {
			saveToFile();
			logger.info("Station supprimée avec succès : {} {}", address, station);
		} else {
			logger.error("Échec de la suppression : Sation/adresse introuvable pour suppression : {} {}", address,
					station);
		}
		return removed;
	}

	public void saveToFile() {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), dataLoader);
		} catch (IOException e) {
			throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
		}
	}
}
