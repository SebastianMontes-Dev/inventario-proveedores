<div align="center">
  <h1>🚀 Inventario de Proveedores</h1>
  <p><strong>Plataforma Empresarial para Gestión de Proveedores</strong></p>
  <p>
    <img src="https://img.shields.io/badge/Spring_Boot-3.5.14-green?style=for-the-badge&logo=spring" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java" alt="Java 17" />
    <img src="https://img.shields.io/badge/Angular-17-red?style=for-the-badge&logo=angular" alt="Angular 17" />
    <img src="https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql" alt="MySQL" />
  </p>
</div>

<hr />

## 📖 Descripción del Proyecto

**Inventario de Proveedores** es una solución tecnológica completa (End-to-End) diseñada para simplificar y optimizar la administración del ciclo de vida de los proveedores. Con un enfoque en la escalabilidad, seguridad y una experiencia de usuario moderna, este sistema centraliza todos los datos críticos y facilita el control del inventario.

---

## 🏗️ Arquitectura de la Solución

El sistema se compone de una arquitectura basada en capas distribuidas:

- **Backend (API RESTful):** Desarrollado en Java 17 con Spring Boot. Incluye Spring Security y JWT para autenticación, Spring Data JPA para la persistencia y validaciones robustas.
- **Frontend (SPA):** Construido sobre Angular 17 utilizando Angular Material para asegurar una interfaz consistente, responsive y altamente accesible.
- **Base de Datos:** MySQL 8 gestionado y desplegado de manera automatizada a través de contenedores Docker.
- **Documentación de API:** Autogenerada mediante SpringDoc OpenAPI (Swagger).

---

## 🚀 Despliegue Rápido (Quick Start)

Desplegar el proyecto en un entorno local es extremadamente fácil gracias a **Docker Compose**.

### 1. Requisitos Previos
- [Docker](https://www.docker.com/) y Docker Compose instalados.
- JDK 17 y Node.js (v18+) instalados (si se desea levantar en modo desarrollo nativo).

### 2. Levantar la Base de Datos

Ejecuta el siguiente comando en la raíz del proyecto para levantar el contenedor de MySQL con sus respectivos esquemas y datos iniciales:

```bash
docker-compose up -d
```

### 3. Levantar el Backend
Navega a la carpeta `/backend` y ejecuta:

```bash
cd backend
./mvnw spring-boot:run
```
*(El backend se levantará por defecto en `http://localhost:8080`. Documentación Swagger en `http://localhost:8080/swagger-ui.html`)*

### 4. Levantar el Frontend
Navega a la carpeta `/frontend`, instala las dependencias y ejecuta el servidor de desarrollo:

```bash
cd frontend
npm install
npm start
```
*(Accede a la plataforma desde `http://localhost:4200`)*

---

## 🤝 Contribución

¡Las contribuciones son bienvenidas! Revisa el archivo [CONTRIBUTING.md](CONTRIBUTING.md) para conocer los lineamientos, o abre un issue para discutir cualquier mejora o bug.

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.
