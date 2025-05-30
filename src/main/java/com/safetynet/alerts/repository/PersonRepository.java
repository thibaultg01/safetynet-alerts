package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;

import java.util.List;
import java.util.Optional;

/**
 * Interface de dépôt pour la gestion des personnes. Fournit les méthodes CRUD
 * de base pour accéder et manipuler les données de type {@link Person}.
 */
public interface PersonRepository {

	/**
	 * Récupère la liste complète des personnes enregistrées.
	 *
	 * @return une liste de toutes les personnes
	 */
	List<Person> findAll();

	/**
	 * Recherche une personne par son prénom et son nom de famille.
	 *
	 * @param firstName le prénom
	 * @param lastName  le nom de famille
	 * @return un {@link Optional} contenant la personne si elle existe, sinon vide
	 */
	Optional<Person> findByName(String firstName, String lastName);

	/**
	 * Recherche toutes les personnes ayant le nom de famille spécifié.
	 *
	 * @param lastName le nom de famille
	 * @return une liste des personnes ayant ce nom de famille
	 */
	List<Person> findByLastName(String lastName);

	/**
	 * Recherche une personne à partir de son prénom et nom (sans Optional).
	 *
	 * @param firstName le prénom
	 * @param lastName  le nom de famille
	 * @return la personne correspondante, ou null si elle n'est pas trouvée
	 */
	Person findByFirstNameAndLastName(String firstName, String lastName);

	/**
	 * Recherche toutes les personnes résidant à une adresse donnée.
	 *
	 * @param address l’adresse recherchée
	 * @return une liste des personnes à cette adresse
	 */
	List<Person> findByAddress(String address);

	/**
	 * Ajoute une nouvelle personne.
	 *
	 * @param person la personne à sauvegarder
	 */
	void save(Person person);

	/**
	 * Supprime une personne à partir de son prénom et nom.
	 *
	 * @param firstName le prénom
	 * @param lastName  le nom de famille
	 * @return true si la personne a été supprimée, false si elle n’existait pas
	 */
	boolean deleteByName(String firstName, String lastName);

	/**
	 * Met à jour les informations d'une personne existante.
	 *
	 * @param updatedPerson la personne avec les nouvelles informations
	 */
	void update(Person updatedPerson);
}