package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.PersonRepository;
import com.safetynet.alerts.service.impl.CommunityEmailServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class CommunityEmailServiceImplTest {

	private PersonRepository personRepository;
	private CommunityEmailServiceImpl communityEmailService;

	@BeforeEach
	void setUp() {
		personRepository = mock(PersonRepository.class);
		communityEmailService = new CommunityEmailServiceImpl(personRepository);
	}

	@Test
	void getEmailsByCity_shouldReturnDistinctEmails() {
		Person p1 = new Person();
		p1.setFirstName("John");
		p1.setLastName("Doe");
		p1.setCity("Paris");
		p1.setEmail("john@example.com");

		Person p2 = new Person();
		p2.setFirstName("Jane");
		p2.setLastName("Smith");
		p2.setCity("Paris");
		p2.setEmail("jane@example.com");

		Person p3 = new Person();
		p3.setFirstName("John");
		p2.setLastName("Roe");
		p3.setCity("Paris");
		p3.setEmail("john@example.com");

		when(personRepository.findAll()).thenReturn(Arrays.asList(p1, p2, p3));

		List<String> emails = communityEmailService.getEmailsByCity("Paris");

		assertThat(emails).containsExactlyInAnyOrder("john@example.com", "jane@example.com");
		verify(personRepository).findAll();
	}
}