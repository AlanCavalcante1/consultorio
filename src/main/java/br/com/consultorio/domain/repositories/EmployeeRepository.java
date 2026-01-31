package br.com.consultorio.domain.repositories;

import br.com.consultorio.domain.entities.Employee;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  boolean existsByCpf(String cpf);
  Optional<Employee> findByCpf(String cpf);
}