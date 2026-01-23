package com.mrs.pet_project.domain.mapper;

import com.mrs.pet_project.domain.dto.request.RequestCreateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.dto.request.RequestUpdateStatusDto;
import com.mrs.pet_project.domain.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RequestMapper {
  @Mapping(source = "patient.id", target = "patientId")
  @Mapping(source = "doctor.id", target = "doctorId")
  RequestDto toDto(Request request);
  Request toEntity(RequestCreateDto dto);
  Request toEntity(RequestDto requestDto);
  void updateEntityFromDto(RequestUpdateStatusDto dto, @MappingTarget Request request);
}
