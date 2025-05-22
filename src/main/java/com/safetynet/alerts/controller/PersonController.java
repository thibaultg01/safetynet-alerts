package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controleur REST pour la gestion des personnes.
 * Permet d'ajouter, de modifier et de supprimer une personne.
 * 
 *   POST /person : ajoute une nouvelle personne
 *   PUT /person : met à jour une personne existante
 *   DELETE /person : supprime une personne par prénom et nom
 */
@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = LogManager.getLogger(PersonController.class);
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Ajoute une nouvelle personne à la base de données.
     *
     * @param personDto les données de la personne
     * @return 201 Created si l'ajout est un succès
     */
    @PostMapping
    public ResponseEntity<PersonDTO> addPerson(@RequestBody PersonDTO personDto) {
    	if (logger.isDebugEnabled()) {
            logger.debug("Réception de la requête POST /person avec : {}", personDto);
        }

        PersonDTO created = personService.addPerson(personDto);
        logger.info("Personne ajoutée avec succès : {} {}", created.getFirstName(), created.getLastName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Met à jour les informations d'une personne existante.
     *
     * @param personDto les nouvelles données
     * @return 200 OK si la mise à jour est un succès
     */
    @PutMapping
    public ResponseEntity<PersonDTO> updatePerson(@RequestBody PersonDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("Réception de la requête PUT /person avec : {}", dto);
        }

        PersonDTO updated = personService.updatePerson(dto);
        logger.info("Personne mise à jour avec succès : {} {}", updated.getFirstName(), updated.getLastName());
        return ResponseEntity.ok(updated);
    }

    /**
     * Supprime une personne de la base à partir du prénom et du nom.
     *
     * @param firstName prénom de la personne
     * @param lastName nom de famille de la personne
     * @return 204 No Content si la suppression est un succès
     */
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName, @RequestParam String lastName) {
        if (logger.isDebugEnabled()) {
            logger.debug("Réception de la requête DELETE /person avec : {} {}", firstName, lastName);
        }

        personService.deletePerson(firstName, lastName);
        logger.info("Personne supprimée avec succès : {} {}", firstName, lastName);
        return ResponseEntity.noContent().build(); // 204 si suppression réussie
    }
}
