package com.mrs.pet_project.repository;

import com.mrs.pet_project.domain.entity.Doctor;
import com.mrs.pet_project.domain.enums.DoctorProfession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
  List<Doctor> findByDoctorProfession(DoctorProfession profession);
}
