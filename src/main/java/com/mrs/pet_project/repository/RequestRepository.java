package com.mrs.pet_project.repository;

import com.mrs.pet_project.domain.entity.Request;
import com.mrs.pet_project.domain.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
  List<Request> findByDoctorId(Long doctorId);
  List<Request> findByPatientId(Long patientId);
  List<Request> findByDoctorIdAndRequestStatus(Long doctorId, RequestStatus status);
  List<Request> findByPatientIdAndRequestStatus(Long patientId, RequestStatus status);
}
