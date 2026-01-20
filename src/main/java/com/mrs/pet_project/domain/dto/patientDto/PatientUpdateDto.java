package com.mrs.pet_project.domain.dto.patientDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientUpdateDto {

  private String name;

  private String surname;

  @Email
  private String email;

  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  private String number;
}
