package com.safetynet.alerts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(FirestationServiceImpl.class);
	
    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public FirestationServiceImpl(FirestationRepository firestationRepository,
                                  PersonRepository personRepository,
                                  MedicalRecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber) {
        logger.info("Recherche des personnes couvertes par la caserne n°{}", stationNumber);

        List<String> addresses = firestationRepository.getAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            logger.warn("Aucune adresse trouvée pour la caserne n°{}", stationNumber);
        } else if(logger.isDebugEnabled()){
            logger.debug("{} adresse(s) trouvée(s) pour la caserne n°{} : {}", addresses.size(), stationNumber, addresses);
        }

        List<PersonCoveredByStationDTO> personsDTO = new ArrayList<>();
        int adults = 0;
        int children = 0;

        for (Person person : personRepository.findAll()) {
            if (addresses.contains(person.getAddress())) {
                MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(), person.getLastName());

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

        logger.info("Résultat pour la caserne n°{} : {} personnes ({} adultes, {} enfants)", stationNumber, personsDTO.size(), adults, children);

        FirestationCoverageResponseDTO response = new FirestationCoverageResponseDTO();
        response.setPersons(personsDTO);
        response.setNumberOfAdults(adults);
        response.setNumberOfChildren(children);

        return response;
    }
}