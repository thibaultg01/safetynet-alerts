package com.safetynet.alerts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;
import com.safetynet.alerts.dto.PersonCoveredByStationDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.FirestationService;
import com.safetynet.alerts.utils.DateUtils;

@Service
public class FirestationServiceImpl implements FirestationService {

	private static final Logger logger = LogManager.getLogger(FirestationServiceImpl.class);

	private final FirestationRepository firestationRepository;
	private final PersonRepository personRepository;
	private final MedicalRecordRepository medicalRecordRepository;

	public FirestationServiceImpl(FirestationRepository firestationRepository, PersonRepository personRepository,
			MedicalRecordRepository medicalRecordRepository) {
		this.firestationRepository = firestationRepository;
		this.personRepository = personRepository;
		this.medicalRecordRepository = medicalRecordRepository;
	}

	/** {@inheritDoc} */
	@Override
	public FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("Recherche des personnes couvertes par la caserne n°{}", stationNumber);
		}

		List<String> addresses = firestationRepository.getAddressesByStationNumber(stationNumber);

		if (logger.isDebugEnabled()) {
			logger.debug("{} adresse(s) trouvée(s) pour la caserne n°{} : {}", addresses.size(), stationNumber,
					addresses);
		}

		List<PersonCoveredByStationDTO> personsDTO = new ArrayList<>();
		int adults = 0;
		int children = 0;

		for (Person person : personRepository.findAll()) {
			if (addresses.contains(person.getAddress())) {
				MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
						person.getLastName());

				if (record == null) {
					logger.warn("Dossier médical introuvable pour {} {}", person.getFirstName(), person.getLastName());
					continue;
				}

				int age = DateUtils.calculateAge(record.getBirthdate());

				if (age <= 18) {
					children++;
				} else {
					adults++;
				}

				PersonCoveredByStationDTO dto = new PersonCoveredByStationDTO();
				dto.setFirstName(person.getFirstName());
				dto.setLastName(person.getLastName());
				dto.setAddress(person.getAddress());
				dto.setPhone(person.getPhone());

				personsDTO.add(dto);
			}
		}

		FirestationCoverageResponseDTO response = new FirestationCoverageResponseDTO();
		response.setPersons(personsDTO);
		response.setNumberOfAdults(adults);
		response.setNumberOfChildren(children);

		return response;
	}

	/** {@inheritDoc} */
	@Override
	public List<String> getPhoneNumbersByFirestation(int stationNumber) {
		if (logger.isDebugEnabled()) {
			logger.debug("Recherche des adresses associées à la caserne n°{}", stationNumber);
		}

		List<String> addresses = firestationRepository.getAddressesByStationNumber(stationNumber);

		if (logger.isDebugEnabled()) {
			logger.debug("Adresses trouvées pour la caserne n°{} : {}", stationNumber, addresses);
		}

		List<String> phoneNumbers = personRepository.findAll().stream()
				.filter(person -> addresses.contains(person.getAddress())).map(Person::getPhone).distinct()
				.collect(Collectors.toList());

		if (logger.isDebugEnabled()) {
			logger.debug("Numéros de téléphone extraits : {}", phoneNumbers);
		}

		return phoneNumbers;
	}

	/** {@inheritDoc} */
	@Override
	public void addMapping(String address, int station) {
		if (logger.isDebugEnabled()) {
			logger.debug("Ajout d'une nouvelle caserne à l'adresse : {}", address);
		}

		boolean added = firestationRepository.addMapping(address, station);
		if (!added) {
			logger.error("La caserne existe déjà à l'adresse : {}", address);
			throw new IllegalArgumentException("L'attribution de cette adresse existe déjà.");
		}
	}

	@Override
	public void updateMapping(String address, int newStation) {
		if (logger.isDebugEnabled()) {
			logger.debug("Recherche de la caserne à mettre à jour à l'adresse : {}", address);
		}

		boolean updated = firestationRepository.updateMapping(address, newStation);
		;
		if (!updated) {
			logger.error("Caserne introuvable à l'adresse : {}", address);
			throw new IllegalArgumentException("Adresse introuvable.");
		}
	}

	@Override
	public void deleteMapping(String address, Integer station) {
		if (logger.isDebugEnabled()) {
			logger.debug("Suppression de la caserne : adresse={} station={}", address, station);
		}

		boolean deleted = firestationRepository.deleteMapping(address, station);
		if (!deleted) {
			logger.error("Échec de la suppression : caserne introuvable pour adresse={} et station={}", address,
					station);
			throw new IllegalArgumentException("Caserne introuvable.");
		}
	}
}