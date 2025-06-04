package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDTO;

public interface FireService {
	FireResponseDTO getFireInfoByAddress(String address);
}
