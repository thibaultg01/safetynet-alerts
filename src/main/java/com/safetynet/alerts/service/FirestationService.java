package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.FirestationCoverageResponseDTO;

/**
 * Interface de service pour la gestion des casernes de pompiers. Fournit les
 * opérations nécessaires à la gestion des mappages entre adresses et numéros de
 * casernes, ainsi qu'à la récupération d'informations utiles pour les services
 * d'urgence.
 */
public interface FirestationService {

	/**
	 * Récupère les personnes couvertes par une caserne donnée, ainsi que le nombre
	 * d'adultes et d'enfants.
	 *
	 * @param stationNumber le numéro de la caserne
	 * @return un objet DTO contenant les personnes couvertes et le décompte
	 *         adultes/enfants
	 */
	FirestationCoverageResponseDTO getPersonsCoveredByStation(int stationNumber);

	/**
	 * Récupère la liste des numéros de téléphone associés aux adresses couvertes
	 * par une caserne donnée.
	 *
	 * @param stationNumber le numéro de la caserne
	 * @return une liste de numéros de téléphone sans doublons
	 */
	List<String> getPhoneNumbersByFirestation(int stationNumber);

	/**
	 * Ajoute un nouveau mappage entre une adresse et un numéro de caserne.
	 *
	 * @param address l'adresse à associer
	 * @param station le numéro de caserne
	 * @throws IllegalArgumentException si un mappage existe déjà pour cette adresse
	 */
	void addMapping(String address, int station);

	/**
	 * Met à jour le numéro de caserne associé à une adresse existante.
	 *
	 * @param address    l'adresse dont le mappage doit être modifié
	 * @param newStation le nouveau numéro de caserne
	 * @throws IllegalArgumentException si l'adresse n'existe pas dans les mappages
	 */
	void updateMapping(String address, int newStation);

	/**
	 * Supprime un mappage adresse-caserne.
	 *
	 * @param address l'adresse à supprimer
	 * @param station le numéro de caserne à supprimer
	 * @throws IllegalArgumentException si aucun mappage ne correspond à l'adresse
	 *                                  et au numéro de caserne donnés
	 */
	void deleteMapping(String address, Integer station);
}