package com.mrs.pet_project.domain.dto.patient;

public record PatientDto(
        Long id,
        String name,
        String surname,
        String email,
        String number
){}


