package com.mrs.pet_project.service.requestService;

import com.mrs.pet_project.domain.dto.request.RequestCreateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.dto.request.RequestUpdateStatusDto;
import com.mrs.pet_project.domain.entity.Doctor;
import com.mrs.pet_project.domain.entity.Patient;
import com.mrs.pet_project.domain.entity.Request;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.domain.mapper.RequestMapper;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import com.mrs.pet_project.repository.DoctorRepository;
import com.mrs.pet_project.repository.PatientRepository;
import com.mrs.pet_project.repository.RequestRepository;
import com.mrs.pet_project.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

  private final RequestRepository requestRepository;
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final RequestMapper requestMapper;
  private final EmailService emailService;


  @Override
  public RequestDto getById(Long id) {
    Request request = findRequestById(id);
    return requestMapper.toDto(request);
  }

  @Override
  @Transactional
  public RequestDto create(RequestCreateDto dto) {
    log.info("Creating request for patientId {} and doctorId {}", dto.getPatientId(), dto.getDoctorId());

    Patient patient = patientRepository.findById(dto.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + dto.getPatientId()));
    Doctor doctor = doctorRepository.findById(dto.getDoctorId())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorId()));

    Request request = requestMapper.toEntity(dto);
    request.setPatient(patient);
    request.setDoctor(doctor);
    request.setRequestStatus(RequestStatus.NEW);
    request.setCreatedAt(LocalDateTime.now());

    Request saved = requestRepository.save(request);
    log.info("Request created with id: {}", saved.getId());

    emailService.sendRequestEmail(
            patient.getEmail(),
            patient.getName(),
            doctor.getName(),
            saved.getRequestStatus().name(),
            saved.getCreatedAt()
    );
    return requestMapper.toDto(saved);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting request with id: {}", id);
    Request request = findRequestById(id);
    requestRepository.delete(request);
    log.info("Request deleted with id: {}", id);
  }

  @Transactional
  public RequestDto updateStatus(Long id, RequestUpdateStatusDto dto) {
    log.info("Updating status of request {} to {}", id, dto.getRequestStatus());

    Request request = findRequestById(id);

    request.setRequestStatus(dto.getRequestStatus());

    Request updated = requestRepository.save(request);
    log.info("Request {} status updated to {}", id, dto.getRequestStatus());

    return requestMapper.toDto(updated);
  }


  @Override
  public List<RequestDto> search(Long doctorId, RequestStatus status) {
    List<Request> requests;

    if (doctorId != null && status != null) {
      requests = requestRepository.findByDoctorIdAndRequestStatus(doctorId, status);
    } else if (doctorId != null) {
      requests = requestRepository.findByDoctorId(doctorId);
    } else if (status != null) {
      requests = requestRepository.findAll().stream()
              .filter(r -> r.getRequestStatus() == status)
              .collect(Collectors.toList());
    } else {
      requests = requestRepository.findAll();
    }

    return requests.stream()
            .map(requestMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public List<RequestDto> getRequestsForPatient(Long patientId, RequestStatus status) {
    patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

    List<Request> requests;
    if (status != null) {
      requests = requestRepository.findByPatientIdAndRequestStatus(patientId, status);
    } else {
      requests = requestRepository.findByPatientId(patientId);
    }

    return requests.stream()
            .map(requestMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public List<RequestDto> getRequestsForDoctor(Long doctorId, RequestStatus status) {
    doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

    List<Request> requests;
    if (status != null) {
      requests = requestRepository.findByDoctorIdAndRequestStatus(doctorId, status);
    } else {
      requests = requestRepository.findByDoctorId(doctorId);
    }

    return requests.stream()
            .map(requestMapper::toDto)
            .collect(Collectors.toList());
  }

  private Request findRequestById(Long id) {
    return requestRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Request not found with id: " + id));
  }
}
