package br.com.consultorio.domain.services;

import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.api.mappers.EmployeeMapper;
import br.com.consultorio.domain.repositories.EmployeeRepository;
import br.com.consultorio.infra.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeService {
	private final EmployeeRepository employeeRepository;
	private final EmployeeMapper employeeMapper;

	public EmployeeResponseDto createEmployee(CreateEmployeeDto dto) {
		if (!dto.getPassword().equals(dto.getPasswordConfirmation())) {
			throw new PasswordMismatchException("A senha e a confirmação de senha não coincidem.");
		}
		var employee = employeeMapper.toEntity(dto);
		employeeRepository.save(employee);
		return employeeMapper.toResponse(employee);
	}
}
