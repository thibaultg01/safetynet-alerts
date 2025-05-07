package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.CommunityEmailService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final PersonRepository personRepository;
    private static final Logger logger = LoggerFactory.getLogger(CommunityEmailServiceImpl.class);

    public CommunityEmailServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    @Override
    public List<String> getEmailsByCity(String city) {
        List<String> emails = personRepository.findAll().stream()
                .filter(p -> p.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .collect(Collectors.toList());

        if (emails.isEmpty()) {
            logger.info("Aucune adresse email trouvée pour la ville : {}", city);
        } else {
            logger.info("{} emails trouvés pour la ville {} : {}", emails.size(), city, emails);
        }
        return emails;
    }
}