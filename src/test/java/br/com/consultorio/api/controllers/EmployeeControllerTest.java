package br.com.consultorio.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.consultorio.api.dto.CreateEmployeeDto;
import br.com.consultorio.api.dto.EmployeeResponseDto;
import br.com.consultorio.domain.repositories.EmployeeRepository;
import br.com.consultorio.domain.services.EmployeeService;
import br.com.consultorio.domain.services.TokenService;
import br.com.consultorio.infra.exception.CpfAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Teste de Integração da Camada Web (Controller). @WebMvcTest: Carrega apenas o contexto web
 * (Controllers, Advice, Filters), sem subir o servidor completo (Services e Repositories reais
 * ficam de fora). Isso torna o teste muito rápido.
 */
@WebMvcTest(EmployeeController.class)
@ActiveProfiles("test")
class EmployeeControllerTest {

  // O MockMvc é a ferramenta que simula as requisições HTTP (GET, POST, etc.)
  @Autowired private MockMvc mockMvc;

  // ObjectMapper converte nossos Objetos Java para JSON e vice-versa
  @Autowired private ObjectMapper objectMapper;

  // --- MOCKS (Objetos Falsos) ---
  // @MockitoBean substitui o @MockBean (que foi depreciado no Spring Boot 3.4).
  // Ele diz ao Spring: "Não use o Service real, use este mock que eu controlo".
  @MockitoBean private EmployeeService employeeService;

  // Precisamos mockar o TokenService e Repository porque o SecurityConfig
  // e o SecurityFilter dependem deles para iniciar o contexto de segurança.
  @MockitoBean private TokenService tokenService;

  @MockitoBean private EmployeeRepository employeeRepository;

  @Test
  @DisplayName("POST /employees - Deve retornar 201 Created quando o cadastro é bem-sucedido")
  // Simula um usuário logado com a role ADMIN para passar pelo @PreAuthorize
  @WithMockUser(roles = "ADMIN")
  void createEmployee_Success() throws Exception {
    // 1. ARRANGE (Preparação do Cenário)
    // Instanciamos o DTO usando setters para maior clareza e segurança contra erros de ordem
    var dto = createEmployeeDto();

    // Preparamos a resposta que o Mock do Service vai devolver
    EmployeeResponseDto response = new EmployeeResponseDto();
    response.setId(10L);
    response.setName("Maria da Silva");

    // Ensinamos o Mock: "Quando chamarem createEmployee com qualquer coisa, retorne 'response'"
    when(employeeService.createEmployee(any())).thenReturn(response);

    // 2. ACT (Ação) & 3. ASSERT (Validação)
    mockMvc
        .perform(
            post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)) // Converte o DTO para JSON string
                .with(csrf())) // Adiciona o token CSRF (necessário em testes de segurança POST/PUT)

        // Validações
        .andExpect(status().isCreated()) // Espera HTTP 201
        .andExpect(jsonPath("$.id").value(10)) // Verifica se o JSON de resposta tem o ID 10
        .andExpect(jsonPath("$.name").value("Maria da Silva"));
  }

  private CreateEmployeeDto createEmployeeDto() {
    CreateEmployeeDto dto = new CreateEmployeeDto();
    dto.setName("Maria da Silva");
    dto.setCpf("31919790004");
    dto.setEmployeeType(CreateEmployeeDto.EmployeeTypeEnum.NURSE);
    dto.setPassword("123456");
    dto.setPasswordConfirmation("123456");
    return dto;
  }

  @Test
  @DisplayName("POST /employees - Deve retornar 409 Conflict quando o CPF já existe")
  @WithMockUser(roles = "ADMIN")
  void createEmployee_Conflict() throws Exception {
    // 1. ARRANGE
    var dto = createEmployeeDto();

    // Definimos a mensagem de erro esperada
    String mensagemErro = "CPF já cadastrado no sistema";

    // Ensinamos o Mock a lançar a exceção personalizada
    when(employeeService.createEmployee(any()))
        .thenThrow(new CpfAlreadyExistsException(mensagemErro));

    // 2. ACT & ASSERT
    mockMvc
        .perform(
            post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(csrf()))

        // Validações de Erro
        .andExpect(status().isConflict()) // Espera HTTP 409
        // Verifica se o GlobalExceptionHandler capturou o erro e montou o JSON corretamente
        .andExpect(jsonPath("$.fields.cpf").value(mensagemErro));
  }
}