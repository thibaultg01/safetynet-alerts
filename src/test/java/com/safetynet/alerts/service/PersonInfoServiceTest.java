package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDTO;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.repository.MedicalRecordRepository;
import com.safetynet.alerts.service.impl.PersonInfoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PersonInfoServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private PersonInfoServiceImpl personInfoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPersonsByLastName_shouldReturnPersonInfoDTOList() {
        List<PersonInfoDTO> mockResult = List.of(new PersonInfoDTO());
        when(personRepository.findByLastName("Doe")).thenReturn(List.of());

        List<PersonInfoDTO> result = personInfoService.getPersonsByLastName("Doe");

        assertThat(result).isNotNull();
        verify(personRepository).findByLastName("Doe");
    }
}
