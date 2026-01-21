package com.mrs.pet_project.controller;

import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.patient.PatientCreateDto;
import com.mrs.pet_project.domain.dto.patient.PatientDto;
import com.mrs.pet_project.domain.dto.patient.PatientUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.service.patientService.PatientService;
import com.mrs.pet_project.util.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.Patients.BASE)
@RequiredArgsConstructor
@Tag(name = "Patients")
public class PatientController {

  private final PatientService patientService;

  @Operation(summary = "Get patient by ID")
  @GetMapping(EndpointConstants.Patients.BY_ID)
  public PatientDto getById(@PathVariable Long id) {
    return patientService.getById(id);
  }

  @Operation(summary = "Create new patient")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PatientDto create(@RequestBody @Valid PatientCreateDto dto) {
    return patientService.create(dto);
  }

  @Operation(summary = "Update patient")
  @PutMapping(EndpointConstants.Patients.BY_ID)
  public PatientDto update(
          @PathVariable Long id,
          @RequestBody @Valid PatientUpdateDto dto
  ) {
    return patientService.update(id, dto);
  }

  @Operation(summary = "Delete patient")
  @DeleteMapping(EndpointConstants.Patients.BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    patientService.delete(id);
  }

  @Operation(summary = "Get patient's doctors")
  @GetMapping(EndpointConstants.Patients.DOCTORS)
  public List<DoctorDto> getDoctors(@PathVariable Long id) {
    return patientService.getDoctors(id);
  }

  @Operation(summary = "Add doctor to patient")
  @PostMapping(EndpointConstants.Patients.DOCTOR_BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addDoctor(
          @PathVariable Long id,
          @PathVariable Long doctorId
  ) {
    patientService.addDoctor(id, doctorId);
  }

  @Operation(summary = "Remove doctor from patient")
  @DeleteMapping(EndpointConstants.Patients.DOCTOR_BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removeDoctor(
          @PathVariable Long id,
          @PathVariable Long doctorId
  ) {
    patientService.removeDoctor(id, doctorId);
  }

  @Operation(summary = "Get patient's requests")
  @GetMapping(EndpointConstants.Patients.REQUESTS)
  public List<RequestDto> getRequests(
          @PathVariable Long id,
          @RequestParam(required = false)RequestStatus requestStatus
          ) {
    return patientService.getRequests(id, requestStatus);
  }
}