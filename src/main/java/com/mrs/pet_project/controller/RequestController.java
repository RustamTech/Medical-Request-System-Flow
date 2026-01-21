package com.mrs.pet_project.controller;

import com.mrs.pet_project.domain.dto.request.RequestCreateDto;
import com.mrs.pet_project.domain.dto.request.RequestDto;
import com.mrs.pet_project.domain.dto.request.RequestUpdateStatusDto;
import com.mrs.pet_project.domain.enums.RequestStatus;
import com.mrs.pet_project.service.requestService.RequestService;
import com.mrs.pet_project.util.EndpointConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(EndpointConstants.Requests.BASE)
@RequiredArgsConstructor
@Tag(name = "Requests")
public class RequestController {

  private final RequestService requestService;

  @Operation(summary = "Get request by ID")
  @GetMapping(EndpointConstants.Requests.BY_ID)
  public RequestDto getById(@PathVariable Long id) {
    return requestService.getById(id);
  }

  @Operation(summary = "Create new request")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RequestDto create(@RequestBody @Valid RequestCreateDto dto) {
    return requestService.create(dto);
  }

  @Operation(summary = "Update request status")
  @PutMapping(EndpointConstants.Requests.STATUS)
  public RequestDto updateStatus(
          @PathVariable Long id,
          @RequestBody @Valid RequestUpdateStatusDto dto
  ) {
    return requestService.updateStatus(id, dto);
  }

  @Operation(summary = "Delete request")
  @DeleteMapping(EndpointConstants.Requests.BY_ID)
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    requestService.delete(id);
  }

  @Operation(summary = "Search requests")
  @GetMapping
  public List<RequestDto> search(
          @RequestParam(required = false) Long doctorId,
          @RequestParam(required = false) RequestStatus status
  ) {
    return requestService.search(doctorId, status);
  }
}