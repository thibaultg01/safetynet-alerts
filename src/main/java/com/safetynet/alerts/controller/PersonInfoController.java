package com.safetynet.alerts.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.service.PersonInfoService;

@RestController
public class PersonInfoController {

    private final PersonInfoService personInfoService;

    public PersonInfoController(PersonInfoService personInfoService) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDTO> getPersonInfoByLastName(@RequestParam String lastName) {
        return personInfoService.getPersonsByLastName(lastName);
    }
}
