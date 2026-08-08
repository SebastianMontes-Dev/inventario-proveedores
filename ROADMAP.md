<div align="center">
  <h1>🗺️ Hoja de Ruta del Proyecto</h1>
  <p><strong>Inventario de Proveedores</strong></p>
  <img src="https://img.shields.io/badge/Estado-En_Desarrollo-orange?style=for-the-badge" alt="Estado" />
</div>

<hr />

## 🎯 Visión General

Esta hoja de ruta (Roadmap) define los objetivos estratégicos y las fases de desarrollo para la plataforma **Inventario de Proveedores**. Nuestro propósito es escalar la solución desde un gestor básico hasta una herramienta empresarial inteligente y altamente analítica.

---

## 🏃 Fases del Desarrollo

### 🟢 Fase 1: Corto Plazo (1 - 3 Meses)
*Enfoque: Estabilización, mejoras en la experiencia de usuario y funcionalidades esenciales.*

- [x] **Configuración Inicial:** Configuración del repositorio, arquitectura base y contenedores Docker.
- [ ] **Módulo de Evaluación de Proveedores:** Implementar un sistema de calificación por estrellas y reseñas para cada proveedor.
- [ ] **Exportación de Datos:** Añadir la capacidad de exportar el listado de proveedores y sus detalles en formatos **CSV** y **PDF**.
- [ ] **Auditoría Básica:** Registro de acciones de usuarios (creación, edición, eliminación de proveedores) para mayor trazabilidad.
- [ ] **Mejoras de Interfaz:** Optimización de tablas de datos en Angular con paginación avanzada y filtrado dinámico en tiempo real.

### 🟡 Fase 2: Mediano Plazo (3 - 6 Meses)
*Enfoque: Integraciones, notificaciones y métricas empresariales.*

- [ ] **Panel de Control (Dashboard) Analítico:** Creación de un panel principal con gráficos interactivos sobre estados de proveedores, categorías y desempeño utilizando bibliotecas como Chart.js o ECharts.
- [ ] **Sistema de Notificaciones:** Alertas automatizadas por correo electrónico para renovación de contratos de proveedores o evaluación de desempeño.
- [ ] **Integración de Almacenamiento en la Nube:** Permitir la subida de documentos adjuntos (contratos, certificaciones) directamente a Amazon S3 o Azure Blob Storage.
- [ ] **Internacionalización (i18n):** Soporte multi-idioma para la interfaz de usuario (Inglés/Español).
- [ ] **Pruebas Automatizadas E2E:** Implementación de pruebas integrales utilizando herramientas como Cypress o Playwright.

### 🔴 Fase 3: Largo Plazo (6 - 12+ Meses)
*Enfoque: Inteligencia artificial, escalabilidad extrema y servicios avanzados.*

- [ ] **Motor de Recomendación con IA:** Análisis del historial de proveedores para recomendar las mejores opciones de abastecimiento basado en costo-beneficio y confiabilidad.
- [ ] **Arquitectura de Microservicios:** Migración parcial o total del monolito en Spring Boot a microservicios independientes para mejorar la escalabilidad por módulos.
- [ ] **API Pública Segura:** Habilitar endpoints seguros con autenticación OAuth2 para que sistemas de terceros (ERP, CRM) puedan interactuar con el inventario.
- [ ] **Aplicación Móvil Nativa:** Desarrollo de una versión de la aplicación para iOS y Android utilizando React Native o Flutter.

---

## 📣 Participación y Comentarios

La hoja de ruta es un documento vivo que se adapta a las necesidades de los usuarios y del mercado. Si tiene sugerencias o desea proponer una nueva característica, le invitamos a abrir una [incidencia](README.md#🤝-contribuciones) en nuestro repositorio.

<div align="center">
  <sub>Última actualización: Agosto 2026</sub>
</div>
