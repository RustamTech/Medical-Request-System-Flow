package com.mrs.pet_project.controller;

import com.mrs.pet_project.domain.dto.doctor.DoctorCreateDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.enums.DoctorProfession;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.service.doctorService.DoctorService;
import com.mrs.pet_project.util.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.Doctors.BASE)
@RequiredArgsConstructor
@Tag(name = "Doctors")
public class DoctorController {

  private final DoctorService doctorService;

  @Operation(summary = "Get doctor by ID")
  @GetMapping(EndpointConstants.Doctors.BY_ID)
  public DoctorDto getById(@PathVariable Long id) {
    return doctorService.getById(id);
  }

  @Operation(summary = "Create new doctor")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DoctorDto create(@RequestBody @Valid DoctorCreateDto dto) {
    return doctorService.create(dto);
  }

  @Operation(summary = "Update doctor")
  @PutMapping(EndpointConstants.Doctors.BY_ID)
  public DoctorDto update(
          @PathVariable Long id,
          @RequestBody @Valid DoctorUpdateDto dto
  ) {
    return doctorService.update(id, dto);
  }

  @Operation(summary = "Delete doctor")
  @DeleteMapping(EndpointConstants.Doctors.BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    doctorService.delete(id);
  }

  @Operation(summary = "Get doctor's requests")
  @GetMapping(EndpointConstants.Doctors.REQUESTS)
  public List<RequestDto> getRequests(
          @PathVariable Long id,
          @RequestParam(required = false)RequestStatus requestStatus
          ) {
    return doctorService.getRequests(id, requestStatus);
  }

  @Operation(summary = "Find doctors by profession")
  @GetMapping
  public List<DoctorDto> findByProfession(
          @RequestParam(required = false) DoctorProfession profession
  ) {
    return doctorService.findByProfession(profession);
  }
}