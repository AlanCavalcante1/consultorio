package br.com.consultorio.domain.services;

import br.com.consultorio.api.dto.CreatePatientDto;
import br.com.consultorio.api.dto.PatientResponseDto;
import br.com.consultorio.api.mappers.PatientMapper;
import br.com.consultorio.domain.repositories.PatientRepository;
import br.com.consultorio.infra.exception.CpfAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class Patientservice {
  private final PatientRepository patientRepository;
  private final PatientMapper patientMapper;

  public PatientResponseDto createPatient(CreatePatientDto dto) {
    log.info("Iniciando cadastro do Patient {}.", dto.getCpf());
    validateCredentials(dto);
    var patient = patientMapper.toEntity(dto);
    patientRepository.save(patient);
    log.info("Paciente cadastrado com sucesso. ID: {}, CPF: {}", patient.getId(), patient.getCpf());
    return patientMapper.toResponse(patient);
  }

  private void validateCredentials(CreatePatientDto dto) {
    if (patientRepository.existsByCpf(dto.getCpf())) {
      throw new CpfAlreadyExistsException("CPF already exists");
    }
  }
}