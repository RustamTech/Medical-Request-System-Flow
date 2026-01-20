package com.mrs.pet_project.domain.dto.doctorDto;

import com.mrs.pet_project.domain.enums.DoctorProfession;
import lombok.Data;

@Data
public class DoctorDto {

  private Long id;
  private String name;
  private String surname;
  private String email;
  private String number;
  private DoctorProfession doctorProfession;
}
