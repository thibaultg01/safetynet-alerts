package com.safetynet.alerts.repository.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;

import org.springframework.stereotype.Repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JsonPersonRepository implements PersonRepository {

	private static final Logger logger = LogManager.getLogger(JsonPersonRepository.class);
	private final DataLoader dataLoader;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private static final String DATA_FILE_PATH = "src/main/resources/dataTest.json";

	public JsonPersonRepository(DataLoader dataLoader) {
		this.dataLoader = dataLoader;
	}

	@Override
	public List<Person> findAll() {
		return dataLoader.getPersons();
	}

	@Override
	public Optional<Person> findByName(String firstName, String lastName) {
		return dataLoader.getPersons().stream()
				.filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
				.findFirst();
	}

	@Override
	public List<Person> findByLastName(String lastName) {
		return dataLoader.getPersons().stream().filter(p -> p.getLastName().equalsIgnoreCase(lastName))
				.collect(Collectors.toList());
	}

	@Override
	public Person findByFirstNameAndLastName(String firstName, String lastName) {
		return dataLoader.getPersons().stream()
				.filter(p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName))
				.findFirst().orElse(null);
	}

	@Override
	public List<Person> findByAddress(String address) {
		return dataLoader.getPersons().stream().filter(p -> p.getAddress().equalsIgnoreCase(address))
				.collect(Collectors.toList());
	}

	@Override
	public void save(Person person) {
		if (logger.isDebugEnabled()) {
			logger.debug("Ajout de la personne dans la mémoire.");
		}
		dataLoader.getPersons().add(person);
		saveToFile();
		logger.info("Personne enregistrée avec succès : {} {}", person.getFirstName(), person.getLastName());
	}

	@Override
	public void deleteByName(String firstName, String lastName) {
		if (logger.isDebugEnabled()) {
			logger.debug("Suppression de la personne en mémoire.");
		}
		dataLoader.getPersons().removeIf(
				p -> p.getFirstName().equalsIgnoreCase(firstName) && p.getLastName().equalsIgnoreCase(lastName));
		saveToFile();
		logger.info("Personne supprimée avec succès : {} {}", firstName, lastName);
	}

	@Override
	public void update(Person updatedPerson) {
		findByName(updatedPerson.getFirstName(), updatedPerson.getLastName()).ifPresent(person -> {
			person.setAddress(updatedPerson.getAddress());
			person.setCity(updatedPerson.getCity());
			person.setZip(updatedPerson.getZip());
			person.setPhone(updatedPerson.getPhone());
			person.setEmail(updatedPerson.getEmail());
			saveToFile();
		});
	}

	private void saveToFile() {
		try {
			objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE_PATH), dataLoader);
			if (logger.isDebugEnabled()) {
				logger.debug("Fichier dataTest.json mis à jour.");
			}
		} catch (IOException e) {
			logger.error("Erreur lors de la sauvegarde du fichier JSON : {}", e.getMessage(), e);
			throw new RuntimeException("Erreur lors de la sauvegarde du fichier data.json", e);
		}
	}
}