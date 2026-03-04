package com.mrs.pet_project.repository;

import com.mrs.pet_project.domain.entity.MedicalDocument;
import com.mrs.pet_project.domain.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {

  List<MedicalDocument> findByPatient(Patient patient);
  List<MedicalDocument> findByDoctorId(Long doctorId);
  List<MedicalDocument> findByRequestId(Long requestId);
  List<MedicalDocument> findByPatientId(Long patientId);
}
