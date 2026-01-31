package br.com.consultorio.infra.exception;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> fields = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));

    return buildErrorResponse(HttpStatus.BAD_REQUEST, fields);
  }

  @ExceptionHandler(PasswordMismatchException.class)
  public ResponseEntity<Object> handlePasswordMismatch(PasswordMismatchException ex) {
    Map<String, String> fields = new HashMap<>();
    fields.put("passwordConfirmation", ex.getMessage());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, fields);
  }

  @ExceptionHandler(CpfAlreadyExistsException.class)
  public ResponseEntity<Object> handlePasswordMismatch(CpfAlreadyExistsException ex) {
    Map<String, String> fields = new HashMap<>();
    fields.put("cpf", ex.getMessage());
    return buildErrorResponse(HttpStatus.BAD_REQUEST, fields);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
    Map<String, String> fields = new HashMap<>();
    fields.put("login", ex.getMessage());
    return buildErrorResponse(HttpStatus.UNAUTHORIZED, fields);
  }

  private ResponseEntity<Object> buildErrorResponse(HttpStatus status, Map<String, String> fields) {
    Map<String, Object> body = new LinkedHashMap<>();

    body.put("status", status.value());
    body.put("fields", fields);

    return ResponseEntity.status(status).body(body);
  }
}