package br.com.consultorio.api.mappers;

import br.com.consultorio.api.dto.CreatePatientDto;
import br.com.consultorio.api.dto.PatientResponseDto;
import br.com.consultorio.domain.entities.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Patient toEntity(CreatePatientDto dto);

  PatientResponseDto toResponse(Patient entity);
}