package com.mrs.pet_project.domain.dto.medicalDocument;

import com.mrs.pet_project.domain.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentUploadDto {

  private Long patientId;

  private Long doctorId;

  private Long requestId;

  private String description;

  private DocumentType documentType;
}