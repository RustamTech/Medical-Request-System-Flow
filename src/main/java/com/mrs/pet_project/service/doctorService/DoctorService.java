package com.mrs.pet_project.service.doctorService;

import com.mrs.pet_project.domain.dto.doctor.DoctorCreateDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.enums.DoctorProfession;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;

import java.util.List;

public interface DoctorService {

  /**
   * Get doctor by ID
   * @param id doctor ID
   * @return DoctorDto
   * @throws ResourceNotFoundException if doctor not found
   */
  DoctorDto getById(Long id);

  /**
   * Create new doctor
   * @param dto doctor creation data
   * @return created DoctorDto
   * @throws BadRequestException if email already exists
   */
  DoctorDto create(DoctorCreateDto dto);

  /**
   * Delete doctor
   * @param id doctor ID
   * @throws ResourceNotFoundException if doctor already deleted or not found
   */
  void delete (Long id);

  /**
   * Update doctor
   * @param id doctor ID
   * @return dto doctor update data
   * @throws ResourceNotFoundException if doctor not found
   * @throws BadRequestException if email already exists
   */
  DoctorDto update(Long id, DoctorUpdateDto dto);

  /**
   * Get all requests of doctor
   * @param doctorId doctor ID
   * @return list of RequestDto
   * @throws ResourceNotFoundException if doctor not found
   * @throws BadRequestException if doctor dont have any requests
   */
  List<RequestDto> getRequests(Long doctorId, RequestStatus status);


  /**
   * Get doctor by profession
   * @param profession doctorProfession profession
   * @return dto doctor get by profession
   * @throws ResourceNotFoundException if doctor not found
   */
  List<DoctorDto> findByProfession(DoctorProfession profession);
}
