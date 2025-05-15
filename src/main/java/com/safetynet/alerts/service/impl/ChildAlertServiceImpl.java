package com.safetynet.alerts.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.ChildAlertDTO;
import com.safetynet.alerts.dto.HouseholdMemberDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.ChildAlertService;
import com.safetynet.alerts.utils.DateUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ChildAlertServiceImpl implements ChildAlertService {

	private static final Logger logger = LogManager.getLogger(ChildAlertServiceImpl.class);
    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    public ChildAlertServiceImpl(PersonRepository personRepository,
                                 MedicalRecordRepository medicalRecordRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @Override
    public List<ChildAlertDTO> getChildrenByAddress(String address) {
    	logger.info("Recherche des enfants à l'adresse : {}", address);

        List<Person> residents = personRepository.findByAddress(address);

        if (residents.isEmpty()) {
            logger.info("Aucun résident trouvé à l'adresse : {}", address);
            return Collections.emptyList();
        }
        List<ChildAlertDTO> result = new ArrayList<>();

        for (Person resident : residents) {
        	MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(
        		    resident.getFirstName(), resident.getLastName());

        	if (record != null) {
        	    int age = DateUtils.calculateAge(record.getBirthdate());
        	    if(logger.isDebugEnabled()) {
        	    logger.debug("Résident : {} {} - Âge : {}", resident.getFirstName(), resident.getLastName(), age);	
        	    }
        	    
                if (age <= 18) {
                    List<HouseholdMemberDTO> others = residents.stream()
                        .filter(p -> !(p.getFirstName().equalsIgnoreCase(resident.getFirstName())
                                    && p.getLastName().equalsIgnoreCase(resident.getLastName())))
                        .map(p -> new HouseholdMemberDTO(p.getFirstName(), p.getLastName()))
                        .collect(Collectors.toList());

                    ChildAlertDTO child = new ChildAlertDTO(
                        resident.getFirstName(),
                        resident.getLastName(),
                        age,
                        others
                    );

                    result.add(child);
                    logger.info("Enfant trouvé : {} {} ({} ans)", child.getFirstName(), child.getLastName(), age);
                }
            } else {
                logger.warn("Aucun dossier médical trouvé pour : {} {}", resident.getFirstName(), resident.getLastName());
            }
        }

        logger.info("Nombre total d'enfants trouvés : {}", result.size());
        return result;
    }
}