package com.proyecto.inventario.controller;

import com.proyecto.inventario.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@Tag(name = "Dashboard", description = "KPIs, graficos, productos criticos y actividad reciente")
public class DashboardController {
  private final DashboardService service;

  public DashboardController(DashboardService service) {
    this.service = service;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ALMACENISTA')")
  @Operation(summary = "Obtener KPIs basicos", description = "Retorna total de productos, stock bajo, ordenes pendientes y proveedores activos")
  public ResponseEntity<?> kpis() {
    return ResponseEntity.ok(service.kpis());
  }

  @GetMapping("/resumen")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ALMACENISTA')")
  @Operation(summary = "Obtener resumen completo", description = "Retorna KPIs, distribucion de movimientos, top ventas, productos criticos y actividad reciente")
  public ResponseEntity<?> resumen() {
    return ResponseEntity.ok(service.resumen());
  }
}
