package com.mrs.pet_project.domain.mapper;

import com.mrs.pet_project.domain.dto.medicalDocument.MedicalDocumentDto;
import com.mrs.pet_project.domain.entity.MedicalDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicalDocumentMapper {
  @Mapping(source = "patient.id", target = "patientId")
  @Mapping(source = "doctor.id", target = "doctorId")
  @Mapping(source = "request.id", target = "requestId")
  MedicalDocumentDto toDto(MedicalDocument entity);
}