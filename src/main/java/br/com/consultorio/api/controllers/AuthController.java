package br.com.consultorio.api.controllers;

import br.com.consultorio.api.controller.generated.AuthApi;
import br.com.consultorio.api.dto.LoginDto;
import br.com.consultorio.api.dto.LoginResponseDto;
import br.com.consultorio.domain.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
    LoginResponseDto response = employeeService.login(loginDto);
    return ResponseEntity.ok(response);
  }
}