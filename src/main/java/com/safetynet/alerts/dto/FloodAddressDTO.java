package com.safetynet.alerts.dto;

import java.util.List;

public class FloodAddressDTO {
    private String address;
    private List<FloodHouseholdMemberDTO> residents;

    public FloodAddressDTO() {}

    public FloodAddressDTO(String address, List<FloodHouseholdMemberDTO> residents) {
        this.address = address;
        this.residents = residents;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public List<FloodHouseholdMemberDTO> getResidents() { return residents; }
    public void setResidents(List<FloodHouseholdMemberDTO> residents) { this.residents = residents; }
}