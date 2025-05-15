package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.dto.FloodAddressDTO;
import com.safetynet.alerts.dto.FloodHouseholdMemberDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.FloodService;
import com.safetynet.alerts.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@Service
public class FloodServiceImpl implements FloodService {

    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private static final Logger logger = LogManager.getLogger(FloodServiceImpl.class);
    
    public FloodServiceImpl(FirestationRepository firestationRepository,
                            PersonRepository personRepository,
                            MedicalRecordRepository medicalRecordRepository) {
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public List<FloodAddressDTO> getHouseholdsByStations(List<Integer> stations) {
    	logger.info("Début de getHouseholdsByStations avec les numéros de stations : {}", stations);
        Set<String> addresses = firestationRepository.findAddressesByStationNumbers(stations);
        if (logger.isDebugEnabled()) {
            logger.debug("Adresses trouvées pour les stations {} : {}", stations, addresses);
        }
        List<FloodAddressDTO> result = new ArrayList<>();

        for (String address : addresses) {
            List<Person> persons = personRepository.findByAddress(address);
            List<FloodHouseholdMemberDTO> residents = new ArrayList<>();

            for (Person person : persons) {
                MedicalRecord record = medicalRecordRepository
                        .findByFirstNameAndLastName(person.getFirstName(), person.getLastName());
                if (record == null) {
                    logger.warn("Aucun dossier médical trouvé pour {} {}", person.getFirstName(), person.getLastName());
                    continue;
                }
                int age = DateUtils.calculateAge(record.getBirthdate());
                if (logger.isDebugEnabled()) {
                    logger.debug("Âge calculé pour {} {} : {}", person.getFirstName(), person.getLastName(), age);
                }
                FloodHouseholdMemberDTO dto = new FloodHouseholdMemberDTO(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        age,
                        record.getMedications(),
                        record.getAllergies()
                );
                residents.add(dto);
            }

            result.add(new FloodAddressDTO(address, residents));
        }
        
        logger.info("Fin de getHouseholdsByStations. Nombre total d'adresses traitées : {}", result.size());
        return result;
    }
}
