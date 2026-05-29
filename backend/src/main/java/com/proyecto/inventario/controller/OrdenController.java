package com.proyecto.inventario.controller;

import com.proyecto.inventario.dto.Dtos.OrdenRequest;
import com.proyecto.inventario.dto.Dtos.RecepcionRequest;
import com.proyecto.inventario.model.EstadoOrden;
import com.proyecto.inventario.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Ordenes de Compra", description = "Ciclo completo de ordenes: creacion, envio, recepcion y cancelacion")
public class OrdenController {
  private final OrdenService service;

  public OrdenController(OrdenService service) {
    this.service = service;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ALMACENISTA')")
  @Operation(summary = "Listar ordenes", description = "Filtra por estado, proveedor y paginacion")
  public ResponseEntity<?> list(@RequestParam(required = false) EstadoOrden estado,
                                @RequestParam(required = false) Long proveedorId,
                                Pageable pageable) {
    return ResponseEntity.ok(service.list(estado, proveedorId, pageable));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ALMACENISTA')")
  @Operation(summary = "Obtener orden por ID", description = "Retorna la orden con sus detalles completos")
  public ResponseEntity<?> get(@PathVariable Long id) {
    return ResponseEntity.ok(service.get(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Crear orden de compra", description = "Crea una orden en estado BORRADOR con lineas de detalle y sugerencia de ultimo precio")
  public ResponseEntity<?> create(@Valid @RequestBody OrdenRequest request, Authentication auth) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request, auth));
  }

  @PutMapping("/{id}/enviar")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Enviar orden", description = "Cambia el estado de BORRADOR a ENVIADA y notifica al proveedor por email")
  public ResponseEntity<?> enviar(@PathVariable Long id) {
    return ResponseEntity.ok(service.enviar(id));
  }

  @PostMapping("/{id}/recepcion")
  @PreAuthorize("hasAnyRole('ADMIN','ALMACENISTA')")
  @Operation(summary = "Recibir mercancia", description = "Registra recepcion parcial o total. Actualiza stock automaticamente y genera movimientos")
  public ResponseEntity<?> recepcion(@PathVariable Long id, @Valid @RequestBody RecepcionRequest request, Authentication auth) {
    return ResponseEntity.ok(service.recepcion(id, request, auth));
  }

  @PatchMapping("/{id}/cancelar")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Cancelar orden", description = "Cambia el estado a CANCELADA. Solo aplica a ordenes en estado BORRADOR o ENVIADA")
  public ResponseEntity<?> cancelar(@PathVariable Long id) {
    return ResponseEntity.ok(service.cancelar(id));
  }
}
