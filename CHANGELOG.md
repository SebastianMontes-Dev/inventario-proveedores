# Registro de cambios (Changelog)

Todos los cambios notables en este proyecto se documentan aquí.

---

## [1.0.0] — 2025-05

### Agregado

**Backend (Spring Boot 3.5)**
- API REST completa con 11 controladores: productos, proveedores, precios, órdenes de compra, movimientos de inventario, usuarios, autenticación, panel de control (dashboard).
- Autenticación JWT almacenada en cookies `HttpOnly` — no accesible desde JavaScript.
- Control de acceso basado en roles con `@PreAuthorize`: `ADMIN`, `GERENTE`, `ALMACENISTA`.
- Ciclo de vida completo de órdenes de compra: `BORRADOR → ENVIADA → RECIBIDA_PARCIAL → RECIBIDA / CANCELADA`.
- Actualización automática de inventario al recibir órdenes con registro de auditoría (`MovimientoInventario`).
- `GET /api/dashboard/resumen` — KPIs agregados calculados en el servidor para evitar cálculos en el cliente dependientes de la paginación.
- `GET /api/precios/ultimo` — último precio conocido por par proveedor-producto, utilizado para autocompletar formularios de órdenes.
- `GET /api/auth/session` — endpoint de revalidación de sesión utilizado al iniciar la aplicación para sincronizar el estado de `localStorage` con la cookie activa.
- Zona horaria configurable mediante la variable de entorno `APP_TIME_ZONE` (por defecto `America/Bogota`) para filtros de rango de fechas.
- Notificaciones por correo electrónico mediante plantillas Thymeleaf: correo de bienvenida, alerta de stock bajo, orden despachada.
- Swagger UI a través de Springdoc OpenAPI en `/swagger-ui.html`.
- 24 pruebas unitarias y de integración (JUnit 5).

**Reforzamiento de Seguridad**
- Secreto JWT validado al inicio: rechaza valores menores a 32 caracteres o cadenas de texto de prueba conocidas.
- Bloqueo pesimista (`PESSIMISTIC_WRITE`) en transiciones de estado de órdenes para prevenir condiciones de carrera (race conditions).
- `GlobalExceptionHandler` devuelve mensajes genéricos a los clientes, registra el stack trace completo en el servidor.
- `@Transactional` en todas las operaciones de escritura en la capa de servicios.
- `AuthService.create()` ya no envía contraseñas en texto plano en los correos de bienvenida.
- Política de cookies `SameSite` configurable (`Strict` para mismo origen, `None` para origen cruzado + HTTPS).
- `errorInterceptor` diferencia `401` (sesión expirada) de `403` (permisos insuficientes) — ya no cierra la sesión del usuario por errores de permisos.

**Frontend (Angular 17)**
- Componentes independientes (standalone) con rutas de características cargadas perezosamente (lazy-loaded).
- Sistema de diseño Angular Material con tokens CSS personalizados para alternar entre modo oscuro y claro.
- Panel de control (Dashboard) con KPIs en tiempo real, gráfico de distribución de movimientos en Chart.js, productos con mayor actividad, lista de stock bajo y feed de actividad reciente.
- Formularios reactivos para todas las operaciones CRUD.
- `APP_INITIALIZER` que revalida la sesión mediante `/auth/session` antes de que el router se active — evita que datos obsoletos en `localStorage` permitan el paso a usuarios no autenticados.
- Atajos de teclado globales (`?` para ayuda, `g d/p/m/s/r/o/u` para navegación, `Esc` para cerrar diálogos).
- Carga de esqueletos (Skeleton loaders) durante la obtención de datos.
- Diseño completamente responsivo (360 px → 1920 px).
- Accesibilidad: enlaces de salto (skip links), anillos de foco visibles, soporte para `prefers-reduced-motion`.
- Elementos de navegación ocultos según el rol (ej. proveedores y precios ocultos para `ALMACENISTA`).

**Infraestructura**
- Docker Compose para base de datos MySQL 8 local.
- GitHub Actions CI: comprobación de compilación + pruebas del backend en cada PR, comprobación de compilación del frontend en cada PR.
- `.env.example` con todas las variables de entorno requeridas y opcionales documentadas.

### Solucionado

- Error de cascada de inicio de sesión causado por desincronización entre `localStorage` y cookie `HttpOnly` al reiniciar el backend.
- Cascada 401 en el panel de control: `AuthService.logout()` ahora es idempotente; `errorInterceptor` omite el cierre de sesión para solicitudes a `/auth/login` y `/auth/logout`.
- Los ajustes de inventario ahora rechazan cambios de cantidad cero y cambios que dejarían el stock negativo.
- La función `update()` de producto ya no modifica silenciosamente el estado `active` — solo `create()` lo establece.
- Los productos y proveedores inactivos son excluidos de todas las listas operacionales y rechazados en nuevas operaciones.
- El payload TypeScript `unknown` de los formularios de órdenes se reemplazó por los tipos explícitos `OrdenRequest` / `DetalleOrdenRequest`.

---

## Leyenda

- **Agregado** (Added) — nuevas funcionalidades
- **Solucionado** (Fixed) — corrección de errores
- **Modificado** (Changed) — cambios en el comportamiento existente
- **Eliminado** (Removed) — funcionalidades eliminadas
