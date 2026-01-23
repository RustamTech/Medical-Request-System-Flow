package com.mrs.pet_project.domain.dto.doctor;

import com.mrs.pet_project.domain.enums.DoctorProfession;
import com.mrs.pet_project.util.ValidationConstants;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DoctorUpdateDto {

  private String name;
  private String surname;

  @Email
  private String email;

  @Pattern(regexp = ValidationConstants.VALIDATE_PHONE_NUMBER)
  private String number;

  private DoctorProfession doctorProfession;
}
