package br.com.consultorio.infra.security;

import br.com.consultorio.domain.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
  private final TokenService tokenService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    var token = recoverToken(request);

    if (token != null) {
      try {

        var decodedJWT = tokenService.validateToken(token);
        String cpf = decodedJWT.getSubject();
        String role = decodedJWT.getClaim("type").asString();

        // Converte para Authority do Spring
        var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

        var authentication = new UsernamePasswordAuthenticationToken(cpf, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } catch (Exception e) {
        // Token inválido? Apenas ignora e segue, o Spring Security barra depois (403)
        // Opcional: Logar o erro
      }
    }

    filterChain.doFilter(request, response);
  }

  private String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }
}