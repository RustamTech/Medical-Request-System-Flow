package com.mrs.pet_project.domain.mapper;

import com.mrs.pet_project.domain.dto.doctor.DoctorCreateDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorUpdateDto;
import com.mrs.pet_project.domain.entity.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
  DoctorDto toDto(Doctor doctor);
  Doctor toEntity(DoctorCreateDto dto);
  void updateEntityFromDto(DoctorUpdateDto dto, @MappingTarget Doctor doctor);
}
