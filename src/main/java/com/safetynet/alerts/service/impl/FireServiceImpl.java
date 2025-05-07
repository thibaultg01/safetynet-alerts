package com.safetynet.alerts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.FirePersonDTO;
import com.safetynet.alerts.dto.FireResponseDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.FirestationRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.FireService;
import com.safetynet.alerts.utils.DateUtils;

/**
 * Implémentation du service FireService qui fournit les informations
 * liées à une adresse spécifique.
 *
 * Cette classe récupère :
 * - les personnes vivant à une adresse donnée,
 * - le numéro de la caserne de pompiers qui couvre cette adresse,
 * - les dossiers médicaux des personnes,
 * - et calcule leur âge pour constituer une réponse structurée.
 *
 * Elle est utilisée par le contrôleur FireController pour répondre
 * à l’endpoint REST GET /fire.
 */
@Service
public class FireServiceImpl implements FireService {

    private static final Logger logger = LogManager.getLogger(FireServiceImpl.class);

    private final PersonRepository personRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final FirestationRepository firestationRepository;

    public FireServiceImpl(PersonRepository personRepository,
                           MedicalRecordRepository medicalRecordRepository,
                           FirestationRepository firestationRepository) {
        this.personRepository = personRepository;
        this.medicalRecordRepository = medicalRecordRepository;
        this.firestationRepository = firestationRepository;
    }

    /**
     * Récupère les informations complètes pour une adresse :
     * - les personnes qui y vivent,
     * - le numéro de la caserne qui couvre cette adresse,
     * - leur téléphone, âge, médicaments et allergies.
     *
     * @param address l’adresse à interroger
     * @return un objet FireResponseDTO contenant les résidents et la caserne associée
     */
    @Override
    public FireResponseDTO getFireInfoByAddress(String address) {
        logger.info("Requête reçue pour l'adresse : {}", address);

        List<Person> persons = personRepository.findByAddress(address);
        if(logger.isDebugEnabled()) {
        	logger.debug("Nombre de personnes trouvées à l'adresse {} : {}", address, persons.size());
        }

        int stationNumber = firestationRepository.findStationNumberByAddress(address);
        if(logger.isDebugEnabled()) {
        	logger.debug("Numéro de caserne trouvé pour l'adresse {} : {}", address, stationNumber);	
        }

        List<FirePersonDTO> personDTOs = persons.stream().map(person -> {
            MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(
                person.getFirstName(), person.getLastName());

            if (record == null) {
                logger.warn("Aucun dossier médical trouvé pour {} {}", person.getFirstName(), person.getLastName());
                return null;
            }

            int age = DateUtils.calculateAge(record.getBirthdate());
            logger.trace("Âge calculé pour {} {} : {}", person.getFirstName(), person.getLastName(), age);

            return new FirePersonDTO(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                age,
                record.getMedications(),
                record.getAllergies()
            );
        })
        .filter(dto -> dto != null)
        .collect(Collectors.toList());

        logger.info("Réponse générée avec {} résident(s) pour l'adresse {}", personDTOs.size(), address);
        return new FireResponseDTO(stationNumber, personDTOs);
    }
}