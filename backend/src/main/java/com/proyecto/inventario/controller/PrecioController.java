package com.proyecto.inventario.controller;

import com.proyecto.inventario.dto.Dtos.PrecioRequest;
import com.proyecto.inventario.service.PrecioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/precios")
@Tag(name = "Precios", description = "Historial de precios por proveedor-producto")
public class PrecioController {
  private final PrecioService service;

  public PrecioController(PrecioService service) {
    this.service = service;
  }

  @GetMapping("/historial")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE','ALMACENISTA')")
  @Operation(summary = "Consultar historial de precios", description = "Filtra por producto y/o proveedor. Retorna todos los precios registrados ordenados por fecha")
  public ResponseEntity<?> historial(@RequestParam(required = false) Long productoId,
                                     @RequestParam(required = false) Long proveedorId) {
    return ResponseEntity.ok(service.historial(productoId, proveedorId));
  }

  @GetMapping("/ultimo")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Obtener ultimo precio", description = "Retorna el precio mas reciente para una combinacion proveedor-producto")
  public ResponseEntity<?> ultimo(@RequestParam Long productoId, @RequestParam Long proveedorId) {
    return ResponseEntity.ok(service.ultimo(productoId, proveedorId));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Registrar precio", description = "Crea un nuevo precio para un producto de un proveedor activo")
  public ResponseEntity<?> create(@Valid @RequestBody PrecioRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
  }
}
