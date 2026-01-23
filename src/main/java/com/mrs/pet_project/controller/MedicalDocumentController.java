package com.mrs.pet_project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrs.pet_project.domain.dto.medicalDocument.DocumentUploadDto;
import com.mrs.pet_project.domain.dto.medicalDocument.MedicalDocumentDto;
import com.mrs.pet_project.service.minioService.MinioService;
import com.mrs.pet_project.util.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(EndpointConstants.MedicalDocuments.BASE)
@RequiredArgsConstructor
@Tag(name = "Medical Documents", description = "API для управления медицинскими документами")
public class MedicalDocumentController {

  private final MinioService minioService;

  @Operation(
          summary = "Upload medical document",
          description = "Загрузка медицинского документа (PDF, изображения, Word документы). Максимальный размер: 10MB"
  )
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "201",
                  description = "Документ успешно загружен",
                  content = @Content(schema = @Schema(implementation = MedicalDocumentDto.class))
          ),
          @ApiResponse(responseCode = "400", description = "Невалидные данные или неподдерживаемый формат файла"),
          @ApiResponse(responseCode = "404", description = "Patient, Doctor или Request не найден")
  })
  @PostMapping(value = "/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public MedicalDocumentDto uploadDocument(
          @RequestPart("file") MultipartFile file,
          @RequestPart("dto") String dtoJson
  ) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    DocumentUploadDto dto = mapper.readValue(dtoJson, DocumentUploadDto.class);
    return minioService.uploadFile(file, dto);
  }


  @Operation(
          summary = "Download medical document",
          description = "Скачивание медицинского документа по ID"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Документ успешно скачан"),
          @ApiResponse(responseCode = "404", description = "Документ не найден"),
          @ApiResponse(responseCode = "500", description = "Ошибка при скачивании файла")
  })
  @GetMapping(EndpointConstants.MedicalDocuments.BY_ID_DOWNLOAD)
  public ResponseEntity<byte[]> downloadDocument(
          @Parameter(description = "ID документа")
          @PathVariable Long id
  ) {
    log.info("Downloading document with id: {}", id);

    try {
      MedicalDocumentDto document = minioService.getDocumentById(id);
      InputStream inputStream = minioService.downloadFile(id);
      byte[] bytes = IOUtils.toByteArray(inputStream);

      return ResponseEntity.ok()
              .contentType(MediaType.parseMediaType(document.contentType()))
              .header(HttpHeaders.CONTENT_DISPOSITION,
                      "attachment; filename=\"" + document.fileName() + "\"")
              .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(bytes.length))
              .body(bytes);

    } catch (Exception e) {
      log.error("Failed to download document with id: {}", id, e);
      throw new RuntimeException("Failed to download file", e);
    }
  }

  @Operation(
          summary = "Get document metadata by ID",
          description = "Получение метаданных документа без скачивания самого файла"
  )
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Метаданные документа",
                  content = @Content(schema = @Schema(implementation = MedicalDocumentDto.class))
          ),
          @ApiResponse(responseCode = "404", description = "Документ не найден")
  })
  @GetMapping(EndpointConstants.MedicalDocuments.BY_ID)
  public MedicalDocumentDto getDocumentById(
          @Parameter(description = "ID документа")
          @PathVariable Long id
  ) {
    log.info("Getting document metadata for id: {}", id);
    return minioService.getDocumentById(id);
  }

  @Operation(
          summary = "Get all documents for a patient",
          description = "Получение всех документов конкретного пациента"
  )
  @ApiResponses(value = {
          @ApiResponse(
                  responseCode = "200",
                  description = "Список документов пациента"
          ),
          @ApiResponse(responseCode = "404", description = "Пациент не найден")
  })
  @GetMapping(EndpointConstants.MedicalDocuments.BY_PATIENT)
  public List<MedicalDocumentDto> getPatientDocuments(
          @Parameter(description = "ID пациента")
          @PathVariable Long patientId
  ) {
    log.info("Getting documents for patient: {}", patientId);
    return minioService.getPatientDocuments(patientId);
  }

  @Operation(
          summary = "Get all documents uploaded by a doctor",
          description = "Получение всех документов, загруженных конкретным врачом"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Список документов врача"),
          @ApiResponse(responseCode = "404", description = "Врач не найден")
  })
  @GetMapping(EndpointConstants.MedicalDocuments.BY_DOCTOR)
  public List<MedicalDocumentDto> getDoctorDocuments(
          @Parameter(description = "ID врача")
          @PathVariable Long doctorId
  ) {
    log.info("Getting documents uploaded by doctor: {}", doctorId);
    return minioService.getDoctorDocuments(doctorId);
  }

  @Operation(
          summary = "Get all documents related to a request",
          description = "Получение всех документов, связанных с конкретным запросом"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Список документов запроса"),
          @ApiResponse(responseCode = "404", description = "Запрос не найден")
  })
  @GetMapping(EndpointConstants.MedicalDocuments.BY_REQUEST)
  public List<MedicalDocumentDto> getRequestDocuments(
          @Parameter(description = "ID запроса")
          @PathVariable Long requestId
  ) {
    log.info("Getting documents for request: {}", requestId);
    return minioService.getRequestDocuments(requestId);
  }

  @Operation(
          summary = "Delete document",
          description = "Удаление медицинского документа из системы и MinIO"
  )
  @ApiResponses(value = {
          @ApiResponse(responseCode = "204", description = "Документ успешно удален"),
          @ApiResponse(responseCode = "404", description = "Документ не найден"),
          @ApiResponse(responseCode = "500", description = "Ошибка при удалении файла")
  })
  @DeleteMapping(EndpointConstants.MedicalDocuments.BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDocument(
          @Parameter(description = "ID документа")
          @PathVariable Long id
  ) {
    log.info("Deleting document with id: {}", id);
    minioService.deleteFile(id);
  }
}