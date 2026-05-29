package com.proyecto.inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  OpenAPI customOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Sistema de Inventario con Gestion de Proveedores - API v1")
        .version("1.0.0")
        .description("API REST para gestion integral de inventario: productos, proveedores, ordenes de compra, movimientos de stock y dashboard de KPIs. Autenticacion via JWT en cookie HttpOnly.")
        .contact(new Contact()
          .name("Sebastian Montes Olivera")
          .url("https://github.com/SebastianMontes-Dev"))
        .license(new License()
          .name("MIT")
          .url("https://github.com/SebastianMontes-Dev/sistema-inventario-proveedores/blob/main/LICENSE")));
  }
}
