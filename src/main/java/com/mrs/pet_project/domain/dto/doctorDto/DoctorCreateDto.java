package com.mrs.pet_project.domain.dto.doctorDto;

import com.mrs.pet_project.domain.enums.DoctorProfession;
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
  @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$")
  private String number;

  @NotNull
  private DoctorProfession doctorProfession;
}
