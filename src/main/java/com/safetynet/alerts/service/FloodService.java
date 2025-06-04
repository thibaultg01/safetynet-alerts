package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodAddressDTO;
import java.util.List;

public interface FloodService {
	List<FloodAddressDTO> getHouseholdsByStations(List<Integer> stations);
}