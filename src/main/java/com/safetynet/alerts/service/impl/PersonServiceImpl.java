package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Implementation du service metier pour la gestion des personnes.
 * Gere les opérations d'ajout, de mise à jour et de suppression.
 */
@Service
public class PersonServiceImpl implements PersonService {

    private static final Logger logger = LogManager.getLogger(PersonServiceImpl.class);
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /** {@inheritDoc} */
    @Override
    public PersonDTO addPerson(PersonDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("Vérification de l'existence de la personne à ajouter.");
        }
        if (personRepository.findByName(dto.getFirstName(), dto.getLastName()).isPresent()) {
            logger.error("La personne {} {} existe déjà.", dto.getFirstName(), dto.getLastName());
            throw new IllegalArgumentException("La personne existe déjà.");
        }
        personRepository.save(convertToEntity(dto));
        logger.info("Personne ajoutée avec succès : {} {}", dto.getFirstName(), dto.getLastName());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public PersonDTO updatePerson(PersonDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("Recherche de la personne à mettre à jour.");
        }
        Person person = personRepository.findByName(dto.getFirstName(), dto.getLastName())
                .orElseThrow(() -> {
                    logger.error("Personne non trouvée pour la mise à jour : {} {}", dto.getFirstName(), dto.getLastName());
                    return new IllegalArgumentException("Personne introuvable.");
                });

        if (logger.isDebugEnabled()) {
            logger.debug("Mise à jour des champs de la personne.");
        }
        person.setAddress(dto.getAddress());
        person.setCity(dto.getCity());
        person.setZip(dto.getZip());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());

        personRepository.update(person);
        logger.info("Personne mise à jour avec succès : {} {}", dto.getFirstName(), dto.getLastName());
        return dto;
    }

    /** {@inheritDoc} */
    @Override
    public void deletePerson(String firstName, String lastName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Suppression de la personne : {} {}", firstName, lastName);
        }
        personRepository.deleteByName(firstName, lastName);
        logger.info("Suppression réussie pour la personne : {} {}", firstName, lastName);
    }

    private Person convertToEntity(PersonDTO dto) {
        Person person = new Person();
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAddress(dto.getAddress());
        person.setCity(dto.getCity());
        person.setZip(dto.getZip());
        person.setPhone(dto.getPhone());
        person.setEmail(dto.getEmail());
        return person;
    }
}
