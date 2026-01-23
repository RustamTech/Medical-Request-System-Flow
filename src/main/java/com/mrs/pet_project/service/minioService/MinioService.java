package com.mrs.pet_project.service.minioService;


import com.mrs.pet_project.domain.dto.medicalDocument.DocumentUploadDto;
import com.mrs.pet_project.domain.dto.medicalDocument.MedicalDocumentDto;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface MinioService {

  /**
   * Upload file to MinIO and save metadata to database
   * @param file file to upload
   * @param dto document metadata
   * @return created MedicalDocumentDto
   * @throws BadRequestException if file is invalid or empty
   * @throws ResourceNotFoundException if patient, doctor or request not found
   * @throws RuntimeException if file upload fails
   */
  MedicalDocumentDto uploadFile(MultipartFile file, DocumentUploadDto dto);

  /**
   * Download file from MinIO
   * @param documentId document ID
   * @return InputStream of the file
   * @throws ResourceNotFoundException if document not found
   * @throws RuntimeException if file download fails
   */
  InputStream downloadFile(Long documentId);

  /**
   * Delete file from MinIO and database
   * @param documentId document ID
   * @throws ResourceNotFoundException if document not found
   * @throws RuntimeException if file deletion fails
   */
  void deleteFile(Long documentId);

  /**
   * Get document metadata by ID
   * @param id document ID
   * @return MedicalDocumentDto
   * @throws ResourceNotFoundException if document not found
   */
  MedicalDocumentDto getDocumentById(Long id);

  /**
   * Get all documents for a patient
   * @param patientId patient ID
   * @return list of MedicalDocumentDto
   * @throws ResourceNotFoundException if patient not found
   */
  List<MedicalDocumentDto> getPatientDocuments(Long patientId);

  /**
   * Get all documents uploaded by a doctor
   * @param doctorId doctor ID
   * @return list of MedicalDocumentDto
   * @throws ResourceNotFoundException if doctor not found
   */
  List<MedicalDocumentDto> getDoctorDocuments(Long doctorId);

  /**
   * Get all documents related to a request
   * @param requestId request ID
   * @return list of MedicalDocumentDto
   * @throws ResourceNotFoundException if request not found
   */
  List<MedicalDocumentDto> getRequestDocuments(Long requestId);
}