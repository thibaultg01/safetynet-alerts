package com.safetynet.alerts.repository;

import java.util.List;
import java.util.Set;

/**
 * Interface de dépôt pour la gestion des associations entre adresses et numéros
 * de casernes. Fournit les méthodes pour récupérer, ajouter, mettre à jour et
 * supprimer ces associations.
 */
public interface FirestationRepository {

	/**
	 * Retourne toutes les adresses couvertes par une caserne donnée.
	 *
	 * @param stationNumber le numéro de la caserne
	 * @return une liste des adresses associées à cette caserne
	 */
	List<String> getAddressesByStationNumber(int stationNumber);

	/**
	 * Retourne toutes les adresses couvertes par un ensemble de casernes.
	 *
	 * @param stationNumbers une liste de numéros de casernes
	 * @return un ensemble d’adresses couvertes par ces casernes
	 */
	Set<String> findAddressesByStationNumbers(List<Integer> stationNumbers);

	/**
	 * Retourne le numéro de caserne responsable d’une adresse donnée.
	 *
	 * @param address l’adresse à rechercher
	 * @return le numéro de la caserne, ou -1 si l’adresse n’est pas trouvée
	 */
	int findStationNumberByAddress(String address);

	/**
	 * Ajoute une nouvelle association entre une adresse et un numéro de caserne.
	 *
	 * @param address l’adresse à associer
	 * @param station le numéro de caserne
	 * @return true si l’association a été ajoutée, false si elle existait déjà
	 */
	boolean addMapping(String address, int station);

	/**
	 * Met à jour le numéro de caserne associé à une adresse existante.
	 *
	 * @param address l’adresse à mettre à jour
	 * @param station le nouveau numéro de caserne
	 * @return true si la mise à jour a réussi, false si l’adresse n’a pas été
	 *         trouvée
	 */
	boolean updateMapping(String address, int station);

	/**
	 * Supprime une association entre une adresse et un numéro de caserne.
	 *
	 * @param address l’adresse à dissocier
	 * @param station le numéro de caserne à retirer
	 * @return true si l’association a été supprimée, false si elle n’existait pas
	 */
	boolean deleteMapping(String address, Integer station);
}