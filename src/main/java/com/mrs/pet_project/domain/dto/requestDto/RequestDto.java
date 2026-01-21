package com.mrs.pet_project.domain.dto.requestDto;

import com.mrs.pet_project.domain.enums.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestDto {

  private Long id;
  private String information;
  private RequestStatus requestStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long patientId;
  private Long doctorId;
}
