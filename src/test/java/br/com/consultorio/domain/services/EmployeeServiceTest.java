package br.com.consultorio.domain.services;

import static br.com.consultorio.api.dto.CreateEmployeeDto.EmployeeTypeEnum.DOCTOR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.api.mappers.EmployeeMapper;
import br.com.consultorio.domain.entities.Employee;
import br.com.consultorio.domain.repositories.EmployeeRepository;
import br.com.consultorio.infra.exception.CpfAlreadyExistsException;
import br.com.consultorio.infra.exception.PasswordMismatchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class EmployeeServiceTest {

  @InjectMocks // Cria a instância real do Service e injeta os Mocks abaixo nele
  private EmployeeService employeeService;

  @Mock // Cria um objeto falso
  private EmployeeRepository employeeRepository;
  @Mock private EmployeeMapper employeeMapper;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private TokenService tokenService;

  @Test
  @DisplayName("Deve criar funcionário com sucesso quando dados são válidos")
  void createEmployee_Success() {
    // 1. ARRANGE (Cenário)
    CreateEmployeeDto dto = new CreateEmployeeDto();
    dto.setName("João da Silva");
    dto.setCpf("12345678900");
    dto.setEmployeeType(DOCTOR);
    dto.setPassword("123456");
    dto.setPasswordConfirmation("123456");

    Employee employeeEntity = new Employee();
    employeeEntity.setId(1L);
    EmployeeResponseDto responseDto = new EmployeeResponseDto();
    responseDto.setId(1L);

    // Ensinamos os mocks como se comportar
    when(employeeRepository.existsByCpf(any())).thenReturn(false);
    when(employeeMapper.toEntity(any(), any())).thenReturn(employeeEntity);
    when(employeeRepository.save(any())).thenReturn(employeeEntity);
    when(employeeMapper.toResponse(any())).thenReturn(responseDto);

    // 2. ACT (Ação)
    EmployeeResponseDto result = employeeService.createEmployee(dto);

    // 3. ASSERT (Validação)
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(employeeRepository, times(1)).save(any()); // Garante que o save foi chamado
  }

  @Test
  @DisplayName("Deve lançar exceção quando as senhas não conferem")
  void createEmployee_PasswordMismatch() {
    // ARRANGE
    CreateEmployeeDto dto = new CreateEmployeeDto("João", "123", DOCTOR, "senha1", "senha2");

    // ACT & ASSERT
    assertThrows(PasswordMismatchException.class, () -> employeeService.createEmployee(dto));

    // Garante que NUNCA tentou salvar no banco
    verify(employeeRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lançar exceção quando CPF já existe")
  void createEmployee_CpfDuplicated() {
    // ARRANGE
    CreateEmployeeDto dto = new CreateEmployeeDto("João", "12345678900", DOCTOR, "123", "123");

    // Simulamos que o banco disse "Sim, existe"
    when(employeeRepository.existsByCpf(dto.getCpf())).thenReturn(true);

    // ACT & ASSERT
    assertThrows(CpfAlreadyExistsException.class, () -> employeeService.createEmployee(dto));
  }
}