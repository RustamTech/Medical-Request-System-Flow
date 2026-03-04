package com.mrs.pet_project.domain.dto.doctor;

import com.mrs.pet_project.domain.enums.DoctorProfession;

public record DoctorDto (
   Long id,
   String name,
   String surname,
   String email,
   String number,
   DoctorProfession doctorProfession
) {}
