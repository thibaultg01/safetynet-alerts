package com.safetynet.alerts.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire fournissant des méthodes liées aux manipulations de dates.
 * Actuellement, elle permet de calculer l'âge d'une personne à partir de sa date de naissance
 * au format {@code MM/dd/yyyy}.
 */
public class DateUtils {

	/**
     * Calcule l'âge (en années) d'une personne à partir de sa date de naissance.
     *
     * @param birthdate La date de naissance au format MM/dd/yyyy
     * @return L'âge de la personne en années
     * @throws java.time.format.DateTimeParseException si le format de la date est invalide
     */
	public static int calculateAge(String birthdate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		LocalDate birth = LocalDate.parse(birthdate, formatter);
		return Period.between(birth, LocalDate.now()).getYears();
	}
}
