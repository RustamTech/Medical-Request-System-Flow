package com.mrs.pet_project.util;

public final class EndpointConstants {

  private EndpointConstants(){};
  // Main endpoint:
  public static final String API_V1 = "/api/v1";

  // Patient endpoints:
  public static final class Patients {
    public static final String BASE = API_V1 + "/patients";
    public static final String BY_ID = "/{id}";
    public static final String DOCTORS = "/{id}/doctors";
    public static final String DOCTOR_BY_ID = "/{id}/doctors/{doctorId}";
    public static final String REQUESTS = "/{id}/requests";
  }

  // Doctor endpoints:
  public static final class Doctors {
    public static final String BASE = API_V1 + "/doctors";
    public static final String BY_ID = "/{id}";
    public static final String REQUESTS = "/{id}/requests";
  }

  // Request endpoints:
  public static final class Requests {
    public static final String BASE = API_V1 + "/requests";
    public static final String BY_ID = "/{id}";
    public static final String STATUS = "/{id}/status";
  }

  // Medical Document endpoints:
  public static final class MedicalDocuments {
    public static final String BASE = API_V1 + "/documents";
    public static final String BY_ID = "/{id}";
    public static final String BY_ID_DOWNLOAD = "/{id}/download";
    public static final String BY_PATIENT = "/patient/{patientId}";
    public static final String BY_DOCTOR = "/doctor/{doctorId}";
    public static final String BY_REQUEST = "/request/{requestId}";
  }
}
