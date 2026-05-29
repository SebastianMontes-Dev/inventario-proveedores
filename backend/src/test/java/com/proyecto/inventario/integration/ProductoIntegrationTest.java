package com.proyecto.inventario.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proyecto.inventario.dto.Dtos.AjusteStockRequest;
import com.proyecto.inventario.dto.Dtos.ProductoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("Producto Integration Tests")
class ProductoIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private ProductoRequest productoBasico;

  @BeforeEach
  void setUp() {
    productoBasico = new ProductoRequest(
      "Laptop HP", "Laptop HP 15 pulgadas", "LAP001", "Electronica", 10, 5, "unidad", true
    );
  }

  @Nested
  @DisplayName("CRUD de producto")
  @WithMockUser(username = "admin@inventario.local", roles = {"ADMIN"})
  class ProductoCrud {

    @Test
    @DisplayName("Crear producto retorna 201 con el producto creado")
    void crearProducto() throws Exception {
      mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.nombre").value("Laptop HP"))
        .andExpect(jsonPath("$.codigo").value("LAP001"))
        .andExpect(jsonPath("$.categoria").value("Electronica"))
        .andExpect(jsonPath("$.cantidadStock").value(10))
        .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    @DisplayName("Listar productos retorna 200 con paginacion")
    void listarProductos() throws Exception {
      mockMvc.perform(post("/api/v1/productos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(productoBasico)));

      mockMvc.perform(get("/api/v1/productos")
          .param("page", "0")
          .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("Obtener producto por ID retorna 200 con el detalle")
    void obtenerProducto() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      mockMvc.perform(get("/api/v1/productos/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.nombre").value("Laptop HP"));
    }

    @Test
    @DisplayName("Editar producto actualiza los campos permitidos")
    void editarProducto() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      ProductoRequest editado = new ProductoRequest(
        "Laptop HP EliteBook", "Actualizado", "LAP001", "Computacion", 10, 3, "unidad", true
      );

      mockMvc.perform(put("/api/v1/productos/{id}", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(editado)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.nombre").value("Laptop HP EliteBook"))
        .andExpect(jsonPath("$.categoria").value("Computacion"));
    }

    @Test
    @DisplayName("Desactivar producto hace soft-delete")
    void desactivarProducto() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      mockMvc.perform(delete("/api/v1/productos/{id}", id))
        .andExpect(status().isNoContent());

      mockMvc.perform(get("/api/v1/productos/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.activo").value(false));
    }

    @Test
    @DisplayName("Reactivar producto restaura el estado activo")
    void reactivarProducto() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      mockMvc.perform(delete("/api/v1/productos/{id}", id));

      mockMvc.perform(patch("/api/v1/productos/{id}/reactivar", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.activo").value(true));
    }
  }

  @Nested
  @DisplayName("Ajuste de stock")
  @WithMockUser(username = "admin@inventario.local", roles = {"ADMIN"})
  class AjusteStock {

    @Test
    @DisplayName("Ajuste positivo suma al stock")
    void ajustePositivo() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      AjusteStockRequest ajuste = new AjusteStockRequest(5, "Conteo fisico");

      mockMvc.perform(patch("/api/v1/productos/{id}/ajustar-stock", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(ajuste)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cantidadStock").value(15));
    }

    @Test
    @DisplayName("Ajuste negativo resta del stock")
    void ajusteNegativo() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      AjusteStockRequest ajuste = new AjusteStockRequest(-3, "Merma");

      mockMvc.perform(patch("/api/v1/productos/{id}/ajustar-stock", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(ajuste)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.cantidadStock").value(7));
    }

    @Test
    @DisplayName("Ajuste cero es rechazado con 400")
    void ajusteCeroRechazado() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      AjusteStockRequest ajuste = new AjusteStockRequest(0, "Sin cambio");

      mockMvc.perform(patch("/api/v1/productos/{id}/ajustar-stock", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(ajuste)))
        .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Ajuste que deja stock negativo es rechazado")
    void ajusteNegativoExcesivoRechazado() throws Exception {
      String response = mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andReturn().getResponse().getContentAsString();

      Long id = objectMapper.readTree(response).get("id").asLong();

      AjusteStockRequest ajuste = new AjusteStockRequest(-20, "Error");

      mockMvc.perform(patch("/api/v1/productos/{id}/ajustar-stock", id)
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(ajuste)))
        .andExpect(status().isBadRequest());
    }
  }

  @Nested
  @DisplayName("Control de acceso por rol")
  class ControlAcceso {

    @Test
    @DisplayName("Sin autenticacion retorna 401")
    void sinAutenticacion() throws Exception {
      mockMvc.perform(get("/api/v1/productos"))
        .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GERENTE puede listar productos")
    @WithMockUser(username = "gerente@inventario.local", roles = {"GERENTE"})
    void gerentePuedeListar() throws Exception {
      mockMvc.perform(get("/api/v1/productos"))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GERENTE no puede crear productos")
    @WithMockUser(username = "gerente@inventario.local", roles = {"GERENTE"})
    void gerenteNoPuedeCrear() throws Exception {
      mockMvc.perform(post("/api/v1/productos")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(productoBasico)))
        .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("ALMACENISTA puede listar productos")
    @WithMockUser(username = "almacen@inventario.local", roles = {"ALMACENISTA"})
    void almacenistaPuedeListar() throws Exception {
      mockMvc.perform(get("/api/v1/productos"))
        .andExpect(status().isOk());
    }
  }
}
