package com.mrs.pet_project.exception;

import com.mrs.pet_project.domain.dto.error.ErrorResponse;
import com.mrs.pet_project.exception.customExceptions.BadRequestException;
import com.mrs.pet_project.exception.customExceptions.ExternalServiceException;
import com.mrs.pet_project.exception.customExceptions.FileStorageException;
import com.mrs.pet_project.exception.customExceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Resource not found",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }


  @ExceptionHandler(ExternalServiceException.class)
  public ResponseEntity<ErrorResponse> handleExternalServiceException(ExternalServiceException ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.SERVICE_UNAVAILABLE.value(),
            "External service not available",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Bad request",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<ErrorResponse> handleFileStorageException(FileStorageException ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Not enough memory for saving file",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Illegal state exception",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request){
    ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Unexpected error",
            ex.getMessage(),
            request.getRequestURI()
    );
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

