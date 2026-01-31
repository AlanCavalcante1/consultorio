package br.com.consultorio.api.controllers;

import br.com.consultorio.api.controller.generated.AuthApi;
import br.com.consultorio.api.dto.LoginDto;
import br.com.consultorio.api.dto.LoginResponseDto;
import br.com.consultorio.domain.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController implements AuthApi {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<LoginResponseDto> login(LoginDto loginDto) {
    log.info("Tentativa de login para o CPF: {}", loginDto.getCpf());
    LoginResponseDto response = employeeService.login(loginDto);
    log.info("Login bem-sucedido para o CPF: {}", loginDto.getCpf());
    return ResponseEntity.ok(response);
  }
}