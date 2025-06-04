package com.safetynet.alerts.dto;

import java.util.List;

public class FirestationCoverageResponseDTO {
	private List<PersonCoveredByStationDTO> persons;
	private int numberOfAdults;
	private int numberOfChildren;

	public List<PersonCoveredByStationDTO> getPersons() {
		return persons;
	}

	public void setPersons(List<PersonCoveredByStationDTO> persons) {
		this.persons = persons;
	}

	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}
}
