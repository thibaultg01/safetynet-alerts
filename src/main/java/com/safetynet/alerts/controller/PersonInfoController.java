package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;

/**
 * Contrôleur REST exposant un endpoint pour récupérer les informations personnelles et médicales
 * d’un individu à partir de son nom de famille.
 */
@RestController
public class PersonInfoController {

	private final PersonInfoService personInfoService;

	public PersonInfoController(PersonInfoService personInfoService) {
		this.personInfoService = personInfoService;
	}

	 /**
     * Endpoint GET pour récupérer les informations d’une ou plusieurs personnes partageant le même nom de famille.
     *
     * @param lastName le nom de famille (obligatoire)
     * @return une réponse contenant une liste de {@link PersonInfoDTO}
     */
	@GetMapping("/personInfo")
	public List<PersonInfoDTO> getPersonInfoByLastName(@RequestParam String lastName) {
		return personInfoService.getPersonsByLastName(lastName);
	}
}
