package com.proyecto.inventario.controller;

import com.proyecto.inventario.dto.Dtos.ProveedorRequest;
import com.proyecto.inventario.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Proveedores", description = "CRUD de proveedores con validacion de RUC/NIT")
public class ProveedorController {
  private final ProveedorService service;

  public ProveedorController(ProveedorService service) {
    this.service = service;
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Listar proveedores", description = "Filtra por nombre con paginacion")
  public ResponseEntity<?> list(@RequestParam(required = false) String nombre, Pageable pageable) {
    return ResponseEntity.ok(service.list(nombre, pageable));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
  @Operation(summary = "Obtener proveedor por ID", description = "Retorna el detalle completo del proveedor")
  public ResponseEntity<?> get(@PathVariable Long id) {
    return ResponseEntity.ok(service.get(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor con datos de contacto y RUC/NIT")
  public ResponseEntity<?> create(@Valid @RequestBody ProveedorRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.save(request, null));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Editar proveedor", description = "Actualiza los datos de un proveedor existente")
  public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody ProveedorRequest request) {
    return ResponseEntity.ok(service.save(request, id));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
