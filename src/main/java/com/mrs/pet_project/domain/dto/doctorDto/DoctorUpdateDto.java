package com.mrs.pet_project.domain.dto.doctorDto;

import com.mrs.pet_project.domain.enums.DoctorProfession;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DoctorUpdateDto {

  private String name;
  private String surname;

  @Email
  private String email;

  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  private String number;

  private DoctorProfession doctorProfession;
}
