package com.mrs.pet_project.domain.entity;

import com.mrs.pet_project.domain.enums.DoctorProfession;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "doctor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)

public class Doctor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String name;

  @Column(nullable = false, length = 255)
  private String surname;

  @Column(nullable = false, length = 255, unique = true)
  private String email;

  @Column(nullable = false)
  private String number;

  @Enumerated(EnumType.STRING)
  @Column(length = 50)
  private DoctorProfession doctorProfession;

  @ManyToMany(mappedBy = "doctors")
  private Set<Patient> patients;

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.PERSIST, orphanRemoval = true)
  private List<Request> requests;
}
