package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonDTO;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    public ResponseEntity<Void> addPerson(@RequestBody PersonDTO personDto) {
        logger.info("POST /person - Ajout de la personne : {} {}", personDto.getFirstName(), personDto.getLastName());
        try {
            personService.addPerson(personDto);
            return ResponseEntity.status(201).build();
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout de la personne : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour les informations d'une personne existante.
     *
     * @param personDto les nouvelles données
     * @return 200 OK si la mise à jour est un succès
     */
    @PutMapping
    public ResponseEntity<Void> updatePerson(@RequestBody PersonDTO personDto) {
        logger.info("PUT /person - Mise à jour de la personne : {} {}", personDto.getFirstName(), personDto.getLastName());
        try {
            personService.updatePerson(personDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la personne : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
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
        logger.info("DELETE /person - Suppression de la personne : {} {}", firstName, lastName);
        try {
            personService.deletePerson(firstName, lastName);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression de la personne : {}", e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }
}
