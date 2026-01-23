package com.mrs.pet_project.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestCreateDto {

  @NotBlank
  private String information;

  @NotNull
  private Long patientId;

  @NotNull
  private Long doctorId;
}
