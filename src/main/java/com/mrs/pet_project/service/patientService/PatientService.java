package com.mrs.pet_project.service.patientService;

import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.patient.PatientCreateDto;
import com.mrs.pet_project.domain.dto.patient.PatientDto;
import com.mrs.pet_project.domain.dto.patient.PatientUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;

import java.util.List;

public interface PatientService {

  /**
   * Get patient by ID
   * @param id patient ID
   * @return PatientDto
   * @throws ResourceNotFoundException if patient not found
   */
  PatientDto getById(Long id);

  /**
   * Create new patient
   * @param dto patient creation data
   * @return created PatientDto
   * @throws BadRequestException if email already exists
   */
  PatientDto create(PatientCreateDto dto);

  /**
   * Update existing patient
   * @param id patient ID
   * @param dto patient update data
   * @return updated PatientDto
   * @throws ResourceNotFoundException if patient not found
   * @throws BadRequestException if email already exists
   */
  PatientDto update(Long id, PatientUpdateDto dto);

  /**
   * Delete patient by ID
   * @param id patient ID
   * @throws ResourceNotFoundException if patient not found
   */
  void delete(Long id);

  /**
   * Get all doctors associated with a patient
   * @param patientId patient ID
   * @return list of DoctorDto
   * @throws ResourceNotFoundException if patient not found
   */
  List<DoctorDto> getDoctors(Long patientId);

  /**
   * Add doctor to patient
   * @param patientId patient ID
   * @param doctorId doctor ID
   * @throws ResourceNotFoundException if patient or doctor not found
   * @throws BadRequestException if association already exists
   */
  void addDoctor(Long patientId, Long doctorId);

  /**
   * Remove doctor from patient
   * @param patientId patient ID
   * @param doctorId doctor ID
   * @throws ResourceNotFoundException if patient or doctor not found
   */
  void removeDoctor(Long patientId, Long doctorId);

  /**
   * Get patient's requests, optionally filtered by status
   * @param patientId patient ID
   * @param requestStatus status (optional)
   * @return list of RequestDto
   * @throws ResourceNotFoundException if patient not found
   */
  List<RequestDto> getRequests(Long patientId, RequestStatus requestStatus);
}