package com.mrs.pet_project.service.minioService;

import com.mrs.pet_project.config.MinioConfig;
import com.mrs.pet_project.domain.dto.medicalDocument.DocumentUploadDto;
import com.mrs.pet_project.domain.dto.medicalDocument.MedicalDocumentDto;
import com.mrs.pet_project.domain.entity.Doctor;
import com.mrs.pet_project.domain.entity.MedicalDocument;
import com.mrs.pet_project.domain.entity.Patient;
import com.mrs.pet_project.domain.entity.Request;
import com.mrs.pet_project.domain.mapper.MedicalDocumentMapper;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import com.mrs.pet_project.repository.DoctorRepository;
import com.mrs.pet_project.repository.MedicalDocumentRepository;
import com.mrs.pet_project.repository.PatientRepository;
import com.mrs.pet_project.repository.RequestRepository;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MinioServiceImpl implements MinioService {

  private final MinioClient minioClient;
  private final MinioConfig minioConfig;
  private final MedicalDocumentRepository documentRepository;
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final RequestRepository requestRepository;
  private final MedicalDocumentMapper documentMapper;

  /**
   * Initialize MinIO bucket on application startup
   */
  @jakarta.annotation.PostConstruct
  public void init() {
    try {
      boolean found = minioClient.bucketExists(
              BucketExistsArgs.builder()
                      .bucket(minioConfig.getBucketName())
                      .build()
      );

      if (!found) {
        minioClient.makeBucket(
                MakeBucketArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .build()
        );
        log.info("Bucket '{}' created successfully", minioConfig.getBucketName());
      } else {
        log.info("Bucket '{}' already exists", minioConfig.getBucketName());
      }
    } catch (Exception e) {
      log.error("Error initializing MinIO bucket", e);
      throw new RuntimeException("Failed to initialize MinIO", e);
    }
  }

  @Override
  @Transactional
  public MedicalDocumentDto uploadFile(MultipartFile file, DocumentUploadDto dto) {
    log.info("Uploading file: {} for patient: {}", file.getOriginalFilename(), dto.getPatientId());

    validateFile(file);

    Patient patient = findPatientById(dto.getPatientId());

    Doctor doctor = dto.getDoctorId() != null ?
            findDoctorById(dto.getDoctorId()) : null;

    Request request = dto.getRequestId() != null ?
            findRequestById(dto.getRequestId()) : null;

    String objectName = generateObjectName(file.getOriginalFilename());

    try (InputStream inputStream = file.getInputStream()) {
      minioClient.putObject(
              PutObjectArgs.builder()
                      .bucket(minioConfig.getBucketName())
                      .object(objectName)
                      .stream(inputStream, file.getSize(), -1)
                      .contentType(file.getContentType())
                      .build()
      );

      log.info("File uploaded to MinIO: {}", objectName);

      MedicalDocument document = new MedicalDocument();
      document.setFileName(file.getOriginalFilename());
      document.setObjectName(objectName);
      document.setContentType(file.getContentType());
      document.setFileSize(file.getSize());
      document.setPatient(patient);
      document.setDoctor(doctor);
      document.setRequest(request);
      document.setUploadAt(LocalDateTime.now());
      document.setDescription(dto.getDescription());
      document.setDocumentType(dto.getDocumentType());

      MedicalDocument saved = documentRepository.save(document);
      log.info("Document metadata saved to database with id: {}", saved.getId());

      return documentMapper.toDto(saved);

    } catch (Exception e) {
      log.error("MinIO bucket init error: {}", e.getMessage(), e);
      throw new RuntimeException("File upload failed", e);
    }
  }

  @Override
  public InputStream downloadFile(Long documentId) {
    log.info("Downloading file for document id: {}", documentId);

    MedicalDocument document = findDocumentById(documentId);

    try {
      InputStream stream = minioClient.getObject(
              GetObjectArgs.builder()
                      .bucket(minioConfig.getBucketName())
                      .object(document.getObjectName())
                      .build()
      );

      log.info("File downloaded from MinIO: {}", document.getObjectName());
      return stream;

    } catch (Exception e) {
      log.error("Failed to download file from MinIO: {}", document.getObjectName(), e);
      throw new RuntimeException("File download failed", e);
    }
  }

  @Override
  @Transactional
  public void deleteFile(Long documentId) {
    log.info("Deleting document with id: {}", documentId);

    MedicalDocument document = findDocumentById(documentId);

    try {
      minioClient.removeObject(
              RemoveObjectArgs.builder()
                      .bucket(minioConfig.getBucketName())
                      .object(document.getObjectName())
                      .build()
      );

      documentRepository.delete(document);
      log.info("Document deleted successfully: {}", document.getObjectName());

    } catch (Exception e) {
      log.error("Failed to delete file from MinIO: {}", document.getObjectName(), e);
      throw new RuntimeException("File deletion failed", e);
    }
  }

  @Override
  public MedicalDocumentDto getDocumentById(Long id) {
    log.info("Getting document metadata for id: {}", id);

    MedicalDocument document = findDocumentById(id);
    return documentMapper.toDto(document);
  }

  @Override
  public List<MedicalDocumentDto> getPatientDocuments(Long patientId) {
    log.info("Getting documents for patient: {}", patientId);

    Patient patient = findPatientById(patientId);

    return documentRepository.findByPatient(patient).stream()
            .map(documentMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public List<MedicalDocumentDto> getDoctorDocuments(Long doctorId) {
    log.info("Getting documents uploaded by doctor: {}", doctorId);

    Doctor doctor = findDoctorById(doctorId);

    return documentRepository.findByDoctorId(doctorId).stream()
            .map(documentMapper::toDto)
            .collect(Collectors.toList());
  }

  @Override
  public List<MedicalDocumentDto> getRequestDocuments(Long requestId) {
    log.info("Getting documents for request: {}", requestId);

    Request request = findRequestById(requestId);

    return documentRepository.findByRequestId(requestId).stream()
            .map(documentMapper::toDto)
            .collect(Collectors.toList());
  }

  private void validateFile(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      log.error("File is empty");
      throw new BadRequestException("File is empty");
    }

    String contentType = file.getContentType();
    if (contentType == null || !isValidContentType(contentType)) {
      log.error("Invalid file type: {}", contentType);
      throw new BadRequestException(
              "Invalid file type. Only images, PDF, and Word documents are allowed"
      );
    }

    long maxFileSize = 10 * 1024 * 1024; // 10MB
    if (file.getSize() > maxFileSize) {
      log.error("File size exceeds maximum limit: {} bytes", file.getSize());
      throw new BadRequestException("File size exceeds maximum limit of 10MB");
    }

    log.info("File validation passed: {} ({} bytes)", file.getOriginalFilename(), file.getSize());
  }

  private boolean isValidContentType(String contentType) {
    return contentType.startsWith("image/") ||
            contentType.equals("application/pdf") ||
            contentType.equals("application/msword") ||
            contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
  }

  private String generateObjectName(String originalFilename) {
    String extension = "";
    int lastDot = originalFilename.lastIndexOf('.');
    if (lastDot > 0) {
      extension = originalFilename.substring(lastDot);
    }
    return UUID.randomUUID().toString() + extension;
  }

  private MedicalDocument findDocumentById(Long id) {
    return documentRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Document not found with id: {}", id);
              return new ResourceNotFoundException("Document not found with id: " + id);
            });
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

  private Request findRequestById(Long id) {
    return requestRepository.findById(id)
            .orElseThrow(() -> {
              log.error("Request not found with id: {}", id);
              return new ResourceNotFoundException("Request not found with id: " + id);
            });
  }
}