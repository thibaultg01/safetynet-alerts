package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonDTO;

/**
 * Service définissant les opérations CRUD pour la gestion des personnes.
 *
 * Cette interface regroupe les actions possibles sur les objets Person :
 * - Ajout d'une nouvelle personne
 * - Mise à jour des informations d'une personne existante
 * - Suppression d'une personne par prénom et nom
 *
 * Les implémentations doivent garantir la validation métier,
 * et déléguer les accès aux données via le repository associé.
 */
public interface PersonService {
	/**
     * Ajoute une nouvelle personne au système.
     *
     * @param personDto les données de la personne à ajouter
     * @return l'objet PersonDTO représentant la personne ajoutée
     * @throws IllegalArgumentException si la personne existe déjà
     */
    PersonDTO addPerson(PersonDTO personDto);

    /**
     * Met à jour les informations d'une personne existante.
     * Le prénom et le nom servent d'identifiants uniques et ne peuvent être modifiés.
     *
     * @param personDto les nouvelles données de la personne
     * @return l'objet PersonDTO représentant la personne mise à jour
     * @throws IllegalArgumentException si la personne n'existe pas
     */
    PersonDTO updatePerson(PersonDTO personDto);

    /**
     * Supprime une personne du système à partir de son prénom et de son nom.
     *
     * @param firstName le prénom de la personne à supprimer
     * @param lastName le nom de famille de la personne à supprimer
     * @throws IllegalArgumentException si la personne n'existe pas
     */
    void deletePerson(String firstName, String lastName);
}