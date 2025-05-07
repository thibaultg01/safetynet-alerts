package com.safetynet.alerts.dto;

import java.util.List;

public class FireResponseDTO {
    private int station;
    private List<FirePersonDTO> residents;

    public FireResponseDTO(int station, List<FirePersonDTO> residents) {
        this.station = station;
        this.residents = residents;
    }

	public int getStation() {
		return station;
	}

	public void setStation(int station) {
		this.station = station;
	}

	public List<FirePersonDTO> getResidents() {
		return residents;
	}

	public void setResidents(List<FirePersonDTO> residents) {
		this.residents = residents;
	}

    
}
