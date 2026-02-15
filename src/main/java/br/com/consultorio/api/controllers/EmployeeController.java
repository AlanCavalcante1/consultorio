package br.com.consultorio.api.controllers;

import br.com.consultorio.api.controller.generated.EmployeeApi;
import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.domain.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmployeeController implements EmployeeApi {
  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<EmployeeResponseDto> createEmployee(CreateEmployeeDto dto) {
    EmployeeResponseDto response = employeeService.createEmployee(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}