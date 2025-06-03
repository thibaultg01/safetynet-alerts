package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.CommunityEmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Implémentation du service CommunityEmailService permettant de récupérer les
 * adresses email des personnes résidant dans une ville donnée.
 */
@Service
public class CommunityEmailServiceImpl implements CommunityEmailService {

	private final PersonRepository personRepository;
	private static final Logger logger = LogManager.getLogger(CommunityEmailServiceImpl.class);

	public CommunityEmailServiceImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	/**
	 * Retourne la liste des adresses email de toutes les personnes habitant dans la
	 * ville spécifiée.
	 * 
	 *
	 * @param city le nom de la ville pour laquelle on souhaite récupérer les emails
	 * @return une liste d'adresses email (distinctes) pour la ville donnée ; liste
	 *         vide si aucune correspondance
	 */
	@Override
	public List<String> getEmailsByCity(String city) {
		if (logger.isDebugEnabled()) {
			logger.debug("Recherche des emails pour la ville de {}", city);
		}
		List<String> emails = personRepository.findAll().stream().filter(p -> p.getCity().equalsIgnoreCase(city))
				.map(Person::getEmail).distinct().collect(Collectors.toList());
		logger.info("{} emails trouvés pour la ville {} : {}", emails.size(), city, emails);
		return emails;
	}
}