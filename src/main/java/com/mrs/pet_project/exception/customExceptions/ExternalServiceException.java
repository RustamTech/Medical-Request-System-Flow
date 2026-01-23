package com.mrs.pet_project.exception.customExceptions;

public class ExternalServiceException extends RuntimeException {
  public ExternalServiceException(String message) {
    super(message);
  }
}
