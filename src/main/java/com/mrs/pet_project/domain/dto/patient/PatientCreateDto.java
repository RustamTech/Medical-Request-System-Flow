package com.mrs.pet_project.domain.dto.patient;

import com.mrs.pet_project.util.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PatientCreateDto {

  @NotBlank
  private String name;

  @NotBlank
  private String surname;

  @NotBlank
  @Email
  private String email;

  @Pattern(regexp = ValidationConstants.VALIDATE_PHONE_NUMBER)
  private String number;
}
