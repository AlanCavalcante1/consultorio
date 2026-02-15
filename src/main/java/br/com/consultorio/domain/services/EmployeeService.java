package br.com.consultorio.domain.services;

import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.api.dto.LoginDto;
import br.com.consultorio.api.dto.LoginResponseDto;
import br.com.consultorio.api.mappers.EmployeeMapper;
import br.com.consultorio.domain.repositories.EmployeeRepository;
import br.com.consultorio.infra.exception.CpfAlreadyExistsException;
import br.com.consultorio.infra.exception.InvalidCredentialsException;
import br.com.consultorio.infra.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmployeeService {
  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper;
  private final PasswordEncoder passwordEncoder;
  private final TokenService tokenService;

  public EmployeeResponseDto createEmployee(CreateEmployeeDto dto) {
    log.info("Iniciando cadastro Employee {}. Tipo: {}", dto.getCpf(), dto.getEmployeeType());
    validateCredentials(dto);
    var employee = employeeMapper.toEntity(dto, passwordEncoder);
    employeeRepository.save(employee);
    log.info(
        "Funcionário cadastrado com sucesso. ID: {}, CPF: {}", employee.getId(), employee.getCpf());
    return employeeMapper.toResponse(employee);
  }

  private void validateCredentials(CreateEmployeeDto dto) {
    if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
      throw new PasswordMismatchException("PasswordConfirmation does not match Password");
    }

    if (employeeRepository.existsByCpf(dto.getCpf())) {
      throw new CpfAlreadyExistsException("CPF already exists");
    }
  }

  public LoginResponseDto login(LoginDto dto) {
    var employee =
        employeeRepository.findByCpf(dto.getCpf()).orElseThrow(InvalidCredentialsException::new);

    if (!passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
      throw new InvalidCredentialsException();
    }

    var token = tokenService.generateToken(employee);
    var response = new LoginResponseDto();
    response.setToken(token);
    response.setType("Bearer"); // Padrão OAuth2

    return response;
  }
}