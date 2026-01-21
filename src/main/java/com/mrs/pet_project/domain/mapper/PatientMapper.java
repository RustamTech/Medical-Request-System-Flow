package com.mrs.pet_project.domain.mapper;

import com.mrs.pet_project.domain.dto.patient.PatientCreateDto;
import com.mrs.pet_project.domain.dto.patient.PatientDto;
import com.mrs.pet_project.domain.dto.patient.PatientUpdateDto;
import com.mrs.pet_project.domain.entity.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
  PatientDto toDto(Patient patient);
  Patient toEntity(PatientCreateDto patientDto);
  void updateEntityFromDto(PatientUpdateDto dto, @MappingTarget Patient patient);

}
