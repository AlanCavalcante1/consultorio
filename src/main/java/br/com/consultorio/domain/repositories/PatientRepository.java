package br.com.consultorio.domain.repositories;

import br.com.consultorio.domain.entities.Employee;
import br.com.consultorio.domain.entities.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

  boolean existsByCpf(String cpf);

  Optional<Employee> findByCpf(String cpf);
}