package br.com.consultorio.domain.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.consultorio.domain.entities.Employee;
import br.com.consultorio.domain.enums.EmployeeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest // Sobe o contexto do JPA e usa o H2 automaticamente
class EmployeeRepositoryTest {

  @Autowired private EmployeeRepository employeeRepository;

  @Autowired
  private TestEntityManager
      entityManager; // Útil para "preparar" o banco sem usar o próprio repository

  @Test
  @DisplayName("Deve retornar true se o CPF existir no banco")
  void existsByCpf_True() {
    // ARRANGE (Cenário)
    Employee employee = new Employee();
    employee.setName("Teste");
    employee.setCpf("12345678900");
    employee.setEmployeeType(EmployeeType.ADMIN);
    employee.setPassword("123");

    // Persiste no banco H2 real
    entityManager.persist(employee);

    // ACT
    boolean exists = employeeRepository.existsByCpf("12345678900");

    // ASSERT
    assertThat(exists).isTrue();
  }

  @Test
  @DisplayName("Deve falhar ao tentar salvar CPF duplicado (Constraint do Banco)")
  void save_ThrowsError_WhenCpfExists() {
    // ARRANGE
    Employee emp1 = new Employee();
    emp1.setCpf("11111111111");
    emp1.setName("User 1");
    emp1.setPassword("123");
    emp1.setEmployeeType(EmployeeType.DOCTOR);

    entityManager.persist(emp1); // Salva o primeiro

    // Cria o segundo igual
    Employee emp2 = new Employee();
    emp2.setCpf("11111111111"); // MESMO CPF
    emp2.setName("User 2");
    emp2.setPassword("456");
    emp2.setEmployeeType(EmployeeType.NURSE);

    // ACT & ASSERT
    // Aqui testamos se o BANCO DE DADOS (H2) realmente barra a duplicidade
    assertThrows(
        DataIntegrityViolationException.class,
        () -> {
          employeeRepository.save(emp2);
          employeeRepository.flush(); // Força o envio pro banco agora
        });
  }
}