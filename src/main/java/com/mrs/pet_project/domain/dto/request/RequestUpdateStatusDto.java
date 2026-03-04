package com.mrs.pet_project.domain.dto.request;

import com.mrs.pet_project.domain.enums.RequestStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestUpdateStatusDto {

  @NotNull
  private RequestStatus requestStatus;
}
