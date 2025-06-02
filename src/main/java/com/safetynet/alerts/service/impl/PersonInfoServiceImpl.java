package com.safetynet.alerts.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.PersonInfoService;
import com.safetynet.alerts.utils.DateUtils;

/**
 * Implémentation du service {@link PersonInfoService} permettant de récupérer
 * des informations détaillées sur une personne (âge, email, antécédents
 * médicaux) à partir de son nom de famille.
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

	private final PersonRepository personRepository;
	private final MedicalRecordRepository medicalRecordRepository;

	public PersonInfoServiceImpl(PersonRepository personRepository, MedicalRecordRepository medicalRecordRepository) {
		this.personRepository = personRepository;
		this.medicalRecordRepository = medicalRecordRepository;
	}

	/**
	 * Récupère une liste d’informations détaillées sur les personnes correspondant
	 * à un nom de famille donné.
	 *
	 * @param lastName le nom de famille recherché
	 * @return une liste de {@link PersonInfoDTO} contenant les détails sur chaque
	 *         personne
	 */
	@Override
	public List<PersonInfoDTO> getPersonsByLastName(String lastName) {
		List<Person> persons = personRepository.findByLastName(lastName);

		return persons.stream().map(person -> {
			MedicalRecord record = medicalRecordRepository.findByFirstNameAndLastName(person.getFirstName(),
					person.getLastName());

			PersonInfoDTO dto = new PersonInfoDTO();
			dto.setFirstName(person.getFirstName());
			dto.setLastName(person.getLastName());
			dto.setAddress(person.getAddress());
			dto.setEmail(person.getEmail());

			if (record != null) {
				dto.setAge(DateUtils.calculateAge(record.getBirthdate()));
				dto.setMedications(record.getMedications());
				dto.setAllergies(record.getAllergies());
			}

			return dto;
		}).collect(Collectors.toList());
	}
}
