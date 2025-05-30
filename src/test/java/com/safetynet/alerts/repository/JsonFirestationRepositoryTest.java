package com.safetynet.alerts.repository;

import com.safetynet.alerts.data.DataLoader;
import com.safetynet.alerts.model.Firestation;
import com.safetynet.alerts.repository.impl.JsonFirestationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class JsonFirestationRepositoryTest {

	private JsonFirestationRepository repository;
	private DataLoader dataLoaderMock;
	private List<Firestation> firestations;

	@BeforeEach
	void setUp() {
		firestations = new ArrayList<>();
		firestations.add(new Firestation("123 Main St", 1));
		firestations.add(new Firestation("456 Oak Ave", 2));

		dataLoaderMock = mock(DataLoader.class);
		when(dataLoaderMock.getFirestations()).thenReturn(firestations);

		repository = new JsonFirestationRepository(dataLoaderMock);
	}

	@Test
	void getAddressesByStationNumber_shouldReturnCorrectAddresses() {
		List<String> addresses = repository.getAddressesByStationNumber(1);
		assertThat(addresses).containsExactly("123 Main St");
	}

	@Test
	void findAddressesByStationNumbers_shouldReturnAllMatchingAddresses() {
		Set<String> addresses = repository.findAddressesByStationNumbers(List.of(1, 2));
		assertThat(addresses).containsExactlyInAnyOrder("123 Main St", "456 Oak Ave");
	}

	@Test
	void findStationNumberByAddress_shouldReturnCorrectNumber() {
		int result = repository.findStationNumberByAddress("456 Oak Ave");
		assertThat(result).isEqualTo(2);
	}

	@Test
	void addMapping_shouldAddNewFirestation() {
		Firestation newMapping = new Firestation("789 Elm St", 3);

		JsonFirestationRepository repoSpy = spy(repository);
		doNothing().when(repoSpy).saveToFile();

		boolean result = repoSpy.addMapping("789 Elm St", 3);
		assertThat(result).isTrue();
		assertThat(firestations).anySatisfy(f -> {
			assertThat(f.getAddress()).isEqualTo("789 Elm St");
			assertThat(f.getStation()).isEqualTo(3);
		});
		verify(repoSpy).saveToFile();
	}

	@Test
	void updateMapping_shouldUpdateStationNumber() {
		JsonFirestationRepository repoSpy = spy(repository);
		doNothing().when(repoSpy).saveToFile();

		boolean result = repoSpy.updateMapping("123 Main St", 9);
		assertThat(result).isTrue();
		assertThat(firestations.get(0).getStation()).isEqualTo(9);
		verify(repoSpy).saveToFile();
	}

	@Test
	void deleteMapping_shouldRemoveFirestation() {
		JsonFirestationRepository repoSpy = spy(repository);
		doNothing().when(repoSpy).saveToFile();

		boolean result = repoSpy.deleteMapping("123 Main St", 1);
		assertThat(result).isTrue();
		assertThat(firestations).doesNotContain(new Firestation("123 Main St", 1));
		verify(repoSpy).saveToFile();
	}

	@Test
	void deleteMapping_shouldReturnFalseIfNotFound() {
		boolean result = repository.deleteMapping("unknown", 99);
		assertThat(result).isFalse();
	}
}
