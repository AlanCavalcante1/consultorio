package br.com.consultorio.api.controllers;

import br.com.consultorio.api.controller.generated.PatientApi;
import br.com.consultorio.api.dto.CreatePatientDto;
import br.com.consultorio.api.dto.PatientResponseDto;
import br.com.consultorio.domain.services.Patientservice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PatientController implements PatientApi {
  private final Patientservice patientservice;

  @Override
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<PatientResponseDto> createPatient(CreatePatientDto dto) {
    var response = patientservice.createPatient(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}