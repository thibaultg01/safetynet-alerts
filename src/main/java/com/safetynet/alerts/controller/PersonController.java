package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.DataService;
import com.safetynetalerts.dto.EmailDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/persons")
public class PersonController {

	private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
	private final DataService dataService;

	public PersonController(DataService dataService) {
		this.dataService = dataService;
	}
	@GetMapping
	public ResponseEntity<?> getAllPersons() {
		logger.info("GET /persons called");
		try {
			List<Person> persons = dataService.getAllPersons();
			return ResponseEntity.ok(persons);
		} catch (IOException e) {
			logger.error("Erreur de lecture du fichier data.json", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur de lecture des donnees.");
		}
	}
	@GetMapping("/{firstName}/{lastName}")
	public ResponseEntity<?> getPersonByName(@PathVariable String firstName, @PathVariable String lastName) {
		logger.info("GET /persons/{}/{} called", firstName, lastName);
		try {
			Optional<Person> person = dataService.getPersonByName(firstName, lastName);

			if (person.isPresent()) {
				return ResponseEntity.ok(person.get());
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Personne non trouvée : " + firstName + " " + lastName);
			}
		} catch (IOException e) {
			logger.error("Erreur lors de la lecture du fichier data.json", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur de lecture des donnees.");
		}
	}
	
	
}