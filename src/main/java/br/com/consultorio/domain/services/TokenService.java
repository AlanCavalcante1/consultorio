package br.com.consultorio.domain.services;

import br.com.consultorio.domain.entities.Employee;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  @Value("${api.security.token.issuer}")
  private String issuer;

  public String generateToken(Employee employee) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create()
          .withIssuer(issuer) // Quem emitiu o token (sua API)
          .withSubject(employee.getCpf()) // Quem é o dono do token (ID único)
          .withClaim(
              "type",
              "ROLE_"
                  + employee
                      .getEmployeeType()
                      .toString()) // Dado extra útil para o front (opcional)
          .withExpiresAt(genExpirationDate())
          .sign(algorithm); // Assina digitalmente

    } catch (JWTCreationException exception) {
      throw new JWTCreationException("Error generating JWT token", exception);
    }
  }

  private Instant genExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }

  public DecodedJWT validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);
      return JWT.require(algorithm)
              .withIssuer(issuer)
              .build()
              .verify(token);
    } catch (JWTVerificationException exception) {
      throw new JWTVerificationException("Token inválido ou expirado");
    }
  }
}