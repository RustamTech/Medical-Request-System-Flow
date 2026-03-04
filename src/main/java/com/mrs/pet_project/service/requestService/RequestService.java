package com.mrs.pet_project.service.requestService;

import com.mrs.pet_project.domain.dto.request.RequestCreateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.dto.request.RequestUpdateStatusDto;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;

import java.util.List;

public interface RequestService {

  /**
   * Get request by ID
   * @param id request ID
   * @return RequestDto
   * @throws ResourceNotFoundException if request not found
   */
  RequestDto getById(Long id);

  /**
   * Create new Request
   * @param dto request creation data
   * @return created RequestDto
   * @throws BadRequestException if request already exists
   */
  RequestDto create(RequestCreateDto dto);

  /**
   * Delete request
   * @param id request ID
   * @throws ResourceNotFoundException if request not found
   */
  void delete(Long id);

  /**
   * Update status of request
   * @param id request ID
   * @param dto new status data
   * @return updated RequestDto
   * @throws ResourceNotFoundException if request not found
   */
  RequestDto updateStatus(Long id, RequestUpdateStatusDto dto);

  /**
   * Search requests by doctorId and/or status
   * @param doctorId filter by doctor (optional)
   * @param status filter by status (optional)
   * @return list of RequestDto
   */
  List<RequestDto> search(Long doctorId, RequestStatus status);

  /**
   * Get all requests for a patient
   * @param patientId patient ID
   * @param status filter by status (optional)
   * @return list of RequestDto
   */
  List<RequestDto> getRequestsForPatient(Long patientId, RequestStatus status);

  /**
   * Get all requests for a doctor
   * @param doctorId doctor ID
   * @param status filter by status (optional)
   * @return list of RequestDto
   */
  List<RequestDto> getRequestsForDoctor(Long doctorId, RequestStatus status);
}
