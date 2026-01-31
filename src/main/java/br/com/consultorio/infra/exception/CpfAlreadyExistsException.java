package br.com.consultorio.infra.exception;

public class CpfAlreadyExistsException extends RuntimeException {
  public CpfAlreadyExistsException(String message) {
    super(message);
  }
}