package com.mrs.pet_project.service.patientService;

import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.patient.PatientCreateDto;
import com.mrs.pet_project.domain.dto.patient.PatientDto;
import com.mrs.pet_project.domain.dto.patient.PatientUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.entity.Doctor;
import com.mrs.pet_project.domain.entity.Patient;
import com.mrs.pet_project.domain.entity.Request;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.domain.mapper.DoctorMapper;
import com.mrs.pet_project.domain.mapper.PatientMapper;
import com.mrs.pet_project.domain.mapper.RequestMapper;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import com.mrs.pet_project.repository.DoctorRepository;
import com.mrs.pet_project.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PatientServiceImpl implements PatientService {

  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final PatientMapper patientMapper;
  private final DoctorMapper doctorMapper;
  private final RequestMapper requestMapper;

  @Override
  public PatientDto getById(Long id) {
    log.info("Getting patient by id: {}", id);
    Patient patient = findPatientById(id);
    return patientMapper.toDto(patient);
  }

  @Override
  @Transactional
  public PatientDto create(PatientCreateDto dto) {
    log.info("Creating new patient with email: {}", dto.getEmail());

    if (patientRepository.existsByEmail(dto.getEmail())) {
      log.error("Patient with email {} already exists", dto.getEmail());
      throw new BadRequestException("Patient with email " + dto.getEmail() + " already exists");
    }

    Patient patient = patientMapper.toEntity(dto);
    Patient saved = patientRepository.save(patient);

    log.info("Patient created successfully with id: {}", saved.getId());
    return patientMapper.toDto(saved);
  }

  @Override
  @Transactional
  public PatientDto update(Long id, PatientUpdateDto dto) {
    log.info("Updating patient with id: {}", id);

    Patient patient = findPatientById(id);

    if (dto.getEmail() != null && !dto.getEmail().equals(patient.getEmail())) {
      if (patientRepository.existsByEmail(dto.getEmail())) {
        log.error("Patient with email {} already exists", dto.getEmail());
        throw new BadRequestException("Patient with email " + dto.getEmail() + " already exists");
      }
    }

    patientMapper.updateEntityFromDto(dto, patient);
    Patient updated = patientRepository.save(patient);

    log.info("Patient updated successfully with id: {}", id);
    return patientMapper.toDto(updated);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting patient with id: {}", id);

    Patient patient = findPatientById(id);
    patientRepository.delete(patient);

    log.info("Patient deleted successfully with id: {}", id);
  }

  @Override
  public List<DoctorDto> getDoctors(Long patientId) {
    log.info("Getting doctors for patient with id: {}", patientId);

    Patient patient = findPatientById(patientId);

    return patient.getDoctors().stream()
            .map(doctorMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void addDoctor(Long patientId, Long doctorId) {
    log.info("Adding doctor {} to patient {}", doctorId, patientId);

    Patient patient = findPatientById(patientId);
    Doctor doctor = findDoctorById(doctorId);

    if (patient.getDoctors().contains(doctor)) {
      log.error("Doctor {} is already associated with patient {}", doctorId, patientId);
      throw new BadRequestException("Doctor is already associated with this patient");
    }

    patient.getDoctors().add(doctor);
    log.info("Doctor {} added to patient {} successfully", doctorId, patientId);
  }

  @Override
  @Transactional
  public void removeDoctor(Long patientId, Long doctorId) {
    log.info("Removing doctor {} from patient {}", doctorId, patientId);

    Patient patient = findPatientById(patientId);
    Doctor doctor = findDoctorById(doctorId);

    patient.getDoctors().remove(doctor);
    log.info("Doctor {} removed from patient {} successfully", doctorId, patientId);
  }

  @Override
  public List<RequestDto> getRequests(Long patientId, RequestStatus requestStatus) {
    log.info("Getting requests for patient {} with status: {}", patientId, requestStatus);

    Patient patient = findPatientById(patientId);

    return patient.getRequests().stream()
            .filter(r -> requestStatus == null || r.getRequestStatus() == requestStatus)
            .map(requestMapper::toDto)
            .toList();
  }

  private Patient findPatientById(Long id) {
    return patientRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Patient not found with id: {}", id);
              return new ResourceNotFoundException("Patient not found with id: " + id);
            });
  }

  private Doctor findDoctorById(Long id) {
    return doctorRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Doctor not found with id: {}", id);
              return new ResourceNotFoundException("Doctor not found with id: " + id);
            });
  }
}