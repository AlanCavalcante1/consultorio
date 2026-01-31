package br.com.consultorio.infra.exception;

public class InvalidCredentialsException extends RuntimeException {

  public InvalidCredentialsException() {
    super("User or password invalid");
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}