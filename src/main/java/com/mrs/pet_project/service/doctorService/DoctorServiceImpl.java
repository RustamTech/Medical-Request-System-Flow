package com.mrs.pet_project.service.doctorService;

import com.mrs.pet_project.domain.dto.doctor.DoctorCreateDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorDto;
import com.mrs.pet_project.domain.dto.doctor.DoctorUpdateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.entity.Doctor;
import com.mrs.pet_project.domain.entity.Request;
import com.mrs.pet_project.domain.enums.DoctorProfession;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.domain.mapper.DoctorMapper;
import com.mrs.pet_project.domain.mapper.RequestMapper;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import com.mrs.pet_project.repository.DoctorRepository;
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
public class DoctorServiceImpl implements DoctorService {

  private final DoctorRepository doctorRepository;
  private final DoctorMapper doctorMapper;
  private final RequestMapper requestMapper;

  @Override
  public DoctorDto getById(Long id) {
    log.info("Getting doctor by id: {}", id);
    Doctor doctor = findDoctorById(id);
    return doctorMapper.toDto(doctor);
  }

  @Override
  @Transactional
  public DoctorDto create(DoctorCreateDto dto) {
    log.info("Creating new doctor with email: {}", dto.getEmail());

    if (doctorRepository.existsByEmail(dto.getEmail())) {
      throw new BadRequestException("Doctor with email " + dto.getEmail() + " already exists");
    }

    Doctor doctor = doctorMapper.toEntity(dto);
    Doctor saved = doctorRepository.save(doctor);

    log.info("Doctor created successfully with id: {}", saved.getId());
    return doctorMapper.toDto(saved);
  }

  @Override
  @Transactional
  public DoctorDto update(Long id, DoctorUpdateDto dto) {
    log.info("Updating doctor with id: {}", id);
    Doctor doctor = findDoctorById(id);

    if (dto.getEmail() != null && !dto.getEmail().equals(doctor.getEmail())) {
      if (doctorRepository.existsByEmail(dto.getEmail())) {
        throw new BadRequestException("Doctor with email " + dto.getEmail() + " already exists");
      }
    }

    doctorMapper.updateEntityFromDto(dto, doctor);
    Doctor updated = doctorRepository.save(doctor);

    log.info("Doctor updated successfully with id: {}", updated.getId());
    return doctorMapper.toDto(updated);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    log.info("Deleting doctor with id: {}", id);
    Doctor doctor = findDoctorById(id);
    doctorRepository.delete(doctor);
    log.info("Doctor deleted successfully with id: {}", id);
  }

  @Override
  public List<RequestDto> getRequests(Long doctorId, RequestStatus status) {
    Doctor doctor = doctorRepository.findById(doctorId)
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

    List<Request> requests = doctor.getRequests();

    if (status != null) {
      requests = requests.stream()
              .filter(req -> req.getRequestStatus() == status)
              .collect(Collectors.toList());
    }

    return requests.stream()
            .map(requestMapper::toDto)
            .collect(Collectors.toList());
  }



  @Override
  public List<DoctorDto> findByProfession(DoctorProfession profession) {
    log.info("Finding doctors by profession: {}", profession);
    List<Doctor> doctors = doctorRepository.findByDoctorProfession(profession);

    if (doctors.isEmpty()) {
      throw new ResourceNotFoundException("No doctor found with profession: " + profession);
    }

    return doctors.stream()
            .map(doctorMapper::toDto)
            .collect(Collectors.toList());
  }

  private Doctor findDoctorById(Long id) {
    return doctorRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Doctor not found with id: {}", id);
              return new ResourceNotFoundException("Doctor not found with id: " + id);
            });
  }
}
