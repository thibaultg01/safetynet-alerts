package com.safetynet.alerts.service.impl;

import com.safetynet.alerts.exception.ResourceNotFoundException;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.CommunityEmailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CommunityEmailServiceImpl implements CommunityEmailService {

    private final PersonRepository personRepository;
    private static final Logger logger = LogManager.getLogger(CommunityEmailServiceImpl.class);

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
            logger.error("Aucune adresse email trouvée pour la ville : {}", city);
            throw new ResourceNotFoundException("Aucune adresse email trouvée pour la ville : " + city);
        }
        else
            logger.info("{} emails trouvés pour la ville {} : {}", emails.size(), city, emails);
        
        return emails;
    }
}