package com.mrs.pet_project.domain.dto.doctor;

import com.mrs.pet_project.domain.enums.DoctorProfession;
import com.mrs.pet_project.util.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DoctorCreateDto {

  @NotBlank
  private String name;

  @NotBlank
  private String surname;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Pattern(regexp = ValidationConstants.VALIDATE_PHONE_NUMBER)
  private String number;

  @NotNull
  private DoctorProfession doctorProfession;
}
