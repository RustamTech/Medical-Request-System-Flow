package com.mrs.pet_project.domain.dto.request;

import com.mrs.pet_project.domain.enums.RequestStatus;

import java.time.LocalDateTime;

public record RequestDto (
  Long id,
  String information,
  RequestStatus requestStatus,
  LocalDateTime createdAt,
  LocalDateTime updatedAt,
  Long patientId,
  Long doctorId
) {}
