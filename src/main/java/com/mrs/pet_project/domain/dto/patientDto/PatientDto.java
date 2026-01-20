package com.mrs.pet_project.domain.dto.patientDto;

import lombok.Data;

@Data
public class PatientDto {
  private Long id;
  private String name;
  private String surname;
  private String email;
  private String number;
}
