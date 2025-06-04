package com.safetynet.alerts.service;

import java.util.List;

import com.safetynet.alerts.dto.ChildAlertDTO;

public interface ChildAlertService {
	List<ChildAlertDTO> getChildrenByAddress(String address);
}
