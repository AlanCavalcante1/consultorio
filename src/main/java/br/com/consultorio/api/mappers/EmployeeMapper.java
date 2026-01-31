package br.com.consultorio.api.mappers;

import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.domain.entities.Employee;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "password", source = "password", qualifiedByName = "encryptPassword")
  Employee toEntity(CreateEmployeeDto dto, @Context PasswordEncoder passwordEncoder);

  EmployeeResponseDto toResponse(Employee entity);

  @Named("encryptPassword")
  default String encryptPassword(String password, @Context PasswordEncoder passwordEncoder) {
    return passwordEncoder.encode(password);
  }
}