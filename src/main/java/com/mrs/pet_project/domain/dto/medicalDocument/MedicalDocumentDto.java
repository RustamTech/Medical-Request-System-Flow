package com.mrs.pet_project.domain.dto.medicalDocument;

import com.mrs.pet_project.domain.enums.DocumentType;

import java.time.LocalDateTime;

public record MedicalDocumentDto(
        Long id,
        String fileName,
        String objectName,
        String contentType,
        Long fileSize,
        Long patientId,
        Long doctorId,
        Long requestId,
        String description,
        LocalDateTime uploadAt,
        DocumentType documentType
) {}
