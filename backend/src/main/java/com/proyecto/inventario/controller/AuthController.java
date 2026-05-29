package com.proyecto.inventario.controller;

import com.proyecto.inventario.dto.Dtos.LoginRequest;
import com.proyecto.inventario.dto.Dtos.LoginResponse;
import com.proyecto.inventario.dto.Dtos.SessionResponse;
import com.proyecto.inventario.security.AuthCookie;
import com.proyecto.inventario.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Inicio de sesion, sesion activa y cierre de sesion con JWT en cookie HttpOnly")
public class AuthController {
  private final AuthService authService;
  private final AuthCookie authCookie;

  public AuthController(AuthService authService, AuthCookie authCookie) {
    this.authService = authService;
    this.authCookie = authCookie;
  }

  @PostMapping("/login")
  @Operation(summary = "Iniciar sesion", description = "Autentica al usuario y retorna un JWT en cookie HttpOnly con datos basicos de sesion")
  @ApiResponse(responseCode = "200", description = "Login exitoso")
  @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    LoginResponse login = authService.login(request);
    return ResponseEntity.ok()
      .header(HttpHeaders.SET_COOKIE, authCookie.create(login.token()).toString())
      .body(new SessionResponse(login.nombre(), login.email(), login.rol()));
  }

  @GetMapping("/session")
  @Operation(summary = "Verificar sesion activa", description = "Valida la cookie de sesion y retorna los datos del usuario si el JWT es valido, o 401 si expiro o es invalido")
  @ApiResponse(responseCode = "200", description = "Sesion valida")
  @ApiResponse(responseCode = "401", description = "Token expirado, ausente o invalido")
  public ResponseEntity<SessionResponse> session(@AuthenticationPrincipal UserDetails user) {
    return ResponseEntity.ok(authService.currentSession(user.getUsername()));
  }

  @PostMapping("/logout")
  @Operation(summary = "Cerrar sesion", description = "Invalida la cookie de sesion en el navegador")
  @ApiResponse(responseCode = "204", description = "Cookie limpiada exitosamente")
  public ResponseEntity<?> logout() {
    return ResponseEntity.noContent()
      .header(HttpHeaders.SET_COOKIE, authCookie.clear().toString())
      .build();
  }
}
