package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.PersonInfoDTO;

public interface PersonInfoService {
    List<PersonInfoDTO> getPersonsByLastName(String lastName);
}
