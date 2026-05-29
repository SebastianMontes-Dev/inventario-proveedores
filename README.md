<div align="center">

# Sistema de Inventario con Gestión de Proveedores

**Aplicación web full-stack para gestión de inventario, proveedores y órdenes de compra**

[![Angular](https://img.shields.io/badge/Angular-17-DD0031?logo=angular&logoColor=white)](https://angular.io/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://docs.docker.com/compose/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

[Inicio Rápido](#-inicio-rápido) · [Arquitectura](#-arquitectura) · [API](#-api-reference) · [Roles](#-control-de-acceso) · [Contribuir](#-contribuir)

</div>

---

## Tabla de Contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Stack Tecnológico](#-stack-tecnológico)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Inicio Rápido](#-inicio-rápido)
  - [Con Docker (recomendado)](#opción-a-con-docker-recomendado)
  - [Instalación Manual](#opción-b-instalación-manual)
- [Configuración](#-configuración)
- [API Reference](#-api-reference)
- [Control de Acceso](#-control-de-acceso)
- [Flujo de Uso](#-flujo-de-uso)
- [Contribuir](#-contribuir)

---

## Descripción

Sistema web empresarial para gestión integral de inventario. Permite registrar productos, controlar stock en tiempo real, gestionar proveedores, registrar precios históricos, crear órdenes de compra y recibir mercancía — con trazabilidad completa de todos los movimientos y un dashboard de KPIs.

Construido como monorepo con separación clara entre backend REST (Spring Boot) y frontend SPA (Angular), comunicados vía JWT en cookies HttpOnly.

---

## Características

### Gestión de Inventario
- Catálogo de productos con código único, categoría, unidad de medida y stock mínimo configurable
- Alertas automáticas de stock bajo
- Registro de entradas y salidas con usuario responsable y referencia
- Ajustes de stock con cantidad con signo (positiva suma, negativa resta)
- Trazabilidad completa: cada cambio de stock genera un `MovimientoInventario`

### Proveedores y Precios
- CRUD de proveedores con datos de contacto y validación de RUC/NIT
- Historial de precios por proveedor-producto
- Sugerencia automática del último precio al crear órdenes

### Órdenes de Compra
- Ciclo completo: Borrador → Enviada → Recibida Parcial → Recibida / Cancelada
- Recepción parcial o total de mercancía
- Stock se actualiza automáticamente al recibir

### Dashboard y Reportes
- KPIs en tiempo real: total de productos, stock bajo, órdenes pendientes
- Gráfico de distribución de movimientos (entradas/salidas/ajustes)
- Top productos por actividad
- Lista de productos con stock crítico
- Feed de actividad reciente

### Seguridad y Usuarios
- Autenticación JWT almacenado en cookie HttpOnly (no accesible desde JS)
- Control de acceso basado en roles: ADMIN, GERENTE, ALMACENISTA
- Gestión de usuarios: crear, activar/desactivar, asignar rol

### Experiencia de Usuario
- Dark Mode y Light Mode con tokens CSS
- Atajos de teclado globales (`?` para ver ayuda, `g d` para dashboard, etc.)
- Skeleton loaders durante cargas
- Diseño responsive (360px → 1920px)
- Accesibilidad: skip links, focus visible, `prefers-reduced-motion`

---

## Stack Tecnológico

### Backend

| Tecnología | Versión | Propósito |
|---|---|---|
| Java | 17 | Lenguaje |
| Spring Boot | 3.5 | Framework principal |
| Spring Security | 6 | Autenticación y autorización |
| JJWT | 0.12.6 | Generación y validación de tokens JWT |
| Spring Data JPA | — | ORM con Hibernate |
| MySQL | 8.0 | Base de datos relacional |
| Springdoc OpenAPI | 2.6 | Documentación automática de API |
| JavaMailSender + Thymeleaf | — | Envío de emails con plantillas |
| Maven | 3.9+ | Gestión de dependencias y build |
| JUnit 5 | — | Testing |

### Frontend

| Tecnología | Versión | Propósito |
|---|---|---|
| Angular | 17.3 | Framework SPA (standalone components) |
| Angular Material | 17.3 | Componentes UI y sistema de diseño |
| TypeScript | 5.4 | Lenguaje (strict mode) |
| RxJS | — | Programación reactiva |
| Chart.js | — | Gráficos del dashboard |
| Jest | 29.7 | Testing unitario |
| Angular CLI | 17.3 | Tooling y build |

### Infraestructura

| Tecnología | Propósito |
|---|---|
| Docker Compose | Base de datos MySQL en contenedor |
| GitHub Actions | CI/CD (workflows configurados) |

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    BROWSER                              │
│  ┌───────────────────────────────────────────────────┐  │
│  │               Angular 17 SPA                      │  │
│  │  ┌──────────┐  ┌──────────┐  ┌─────────────────┐ │  │
│  │  │  core/   │  │features/ │  │   shared/       │ │  │
│  │  │ AuthSvc  │  │dashboard │  │ DataTable       │ │  │
│  │  │ ApiSvc   │  │productos │  │ ConfirmDialog   │ │  │
│  │  │ ThemeSvc │  │ordenes   │  │ NotifySvc       │ │  │
│  │  │ RoleGuard│  │usuarios  │  │ KeyboardShortcuts│ │  │
│  │  └──────────┘  └──────────┘  └─────────────────┘ │  │
│  └───────────────────────────────────────────────────┘  │
│                       HTTP + Cookie HttpOnly             │
└──────────────────────────┬──────────────────────────────┘
                           │ :8080
┌──────────────────────────▼──────────────────────────────┐
│                Spring Boot REST API                     │
│  ┌──────────┐  ┌──────────┐  ┌───────────────────────┐ │
│  │Controller│→ │ Service  │→ │     Repository        │ │
│  │@PreAuthor│  │@Transact │  │ Spring Data JPA       │ │
│  └──────────┘  └──────────┘  └───────────────────────┘ │
│  ┌───────────────────────────────────────────────────┐  │
│  │           Security Layer                         │  │
│  │  JwtAuthFilter → JwtService → SecurityConfig     │  │
│  └───────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────┘
                           │ JDBC
┌──────────────────────────▼──────────────────────────────┐
│                  MySQL 8  (:3307)                       │
│  users · productos · proveedores · precios_proveedor    │
│  ordenes_compra · detalles_orden · movimientos          │
└─────────────────────────────────────────────────────────┘
```

### Flujo de Autenticación

```
Usuario → POST /api/auth/login
       ← Cookie HttpOnly "access_token" (JWT)
       ← JSON { nombre, email, rol }

Requests subsiguientes → Cookie enviada automáticamente por el browser
                      ← JwtAuthFilter valida token en cada request
```

---

## Estructura del Proyecto

```
sistema-inventario-proveedores/
│
├── backend/                        # API REST Spring Boot
│   └── src/main/java/com/proyecto/inventario/
│       ├── config/                 # SecurityConfig, CORS, beans globales
│       ├── controller/             # 11 controladores REST
│       ├── dto/                    # Objetos de transferencia de datos
│       ├── entity/                 # 7 entidades JPA (tablas)
│       ├── exception/              # Excepciones custom con HTTP status
│       ├── model/                  # Enums: Rol, TipoMovimiento, EstadoOrden
│       ├── repository/             # 7 repositorios Spring Data JPA
│       ├── security/               # JwtService, JwtAuthFilter, AuthCookie
│       └── service/                # 8 servicios con lógica de negocio
│
├── frontend/                       # SPA Angular 17
│   └── src/app/
│       ├── core/                   # Servicios globales: auth, api, theme, guards
│       ├── features/               # Módulos con lazy loading
│       │   ├── shell/              # Layout: sidenav + toolbar + router outlet
│       │   ├── dashboard/          # KPIs y gráficos
│       │   ├── login/              # Formulario de autenticación
│       │   ├── productos/          # CRUD + ajuste de stock
│       │   ├── proveedores/        # CRUD de proveedores
│       │   ├── precios/            # Historial de precios
│       │   ├── ordenes/            # Ciclo completo de órdenes de compra
│       │   ├── movimientos/        # Registro de entradas/salidas
│       │   └── usuarios/           # Gestión de usuarios (ADMIN only)
│       └── shared/                 # Componentes reutilizables
│           ├── data-table.component.ts
│           ├── confirm-dialog.component.ts
│           ├── skeleton-table.component.ts
│           ├── page-header.component.ts
│           ├── notify.service.ts
│           └── keyboard-shortcuts.service.ts
│
├── db/                             # Scripts SQL
│   ├── schema.sql                  # Esquema: 8 tablas con constraints e índices
│   └── data.sql                    # Datos iniciales (usuario ADMIN)
│
├── .github/workflows/              # CI/CD con GitHub Actions
│   ├── backend-ci.yml              # Build + tests del backend en cada PR
│   └── frontend-ci.yml             # Build del frontend en cada PR
│
├── docker-compose.yml              # MySQL 8 en Docker
├── .env.example                    # Plantilla de variables de entorno
├── CHANGELOG.md                    # Historial de cambios por versión
└── README.md                       # Este archivo
```

---

## Inicio Rápido

### Prerrequisitos

| Herramienta | Versión mínima |
|---|---|
| Java | 17 |
| Maven | 3.9 |
| Node.js | 20 |
| Docker | 20 (para opción A) |
| MySQL | 8.0 (para opción B) |

---

### Opción A: Con Docker (recomendado)

La base de datos corre en un contenedor; backend y frontend en local.

**1. Levantar MySQL**
```bash
docker compose up -d
```

Esto levanta MySQL 8 en el puerto `3307` con usuario `inventario` / contraseña `inventario123`.

**2. Importar esquema**
```bash
mysql -h 127.0.0.1 -P 3307 -u inventario -pinventario123 < db/schema.sql
mysql -h 127.0.0.1 -P 3307 -u inventario -pinventario123 < db/data.sql
```

**3. Configurar variables de entorno del backend**
```bash
cp backend/.env.example backend/.env
# Editar backend/.env con los valores correctos (ver sección Configuración)
```

**4. Iniciar el backend**
```bash
cd backend
mvn spring-boot:run
# API disponible en http://localhost:8080
# Swagger UI en http://localhost:8080/swagger-ui.html
```

**5. Iniciar el frontend**
```bash
cd frontend
npm install
npm start
# App disponible en http://localhost:4200
```

**6. Iniciar sesión**
- URL: `http://localhost:4200`
- Email: `admin@inventario.local`
- Contraseña: `password`

---

### Opción B: Instalación Manual

**1. Crear base de datos**
```bash
mysql -u root -p -e "CREATE DATABASE inventario_db; CREATE USER 'inventario'@'localhost' IDENTIFIED BY 'inventario123'; GRANT ALL ON inventario_db.* TO 'inventario'@'localhost';"
mysql -u root -p inventario_db < db/schema.sql
mysql -u root -p inventario_db < db/data.sql
```

**2. Configurar y ejecutar backend**
```bash
# Exportar variables (o configurar en backend/.env)
export DB_URL="jdbc:mysql://localhost:3306/inventario_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
export DB_USERNAME=inventario
export DB_PASSWORD=inventario123
export JWT_SECRET=mi-secreto-muy-seguro-de-al-menos-32-chars

cd backend
mvn spring-boot:run
```

**3. Ejecutar frontend**
```bash
cd frontend
npm install
npm start
```

---

## Configuración

### Variables de Entorno (Backend)

Copia `backend/.env.example` como `backend/.env` y ajusta los valores:

| Variable | Descripción | Valor por defecto | Requerida |
|---|---|---|---|
| `DB_URL` | URL JDBC de MySQL | — | Sí |
| `DB_USERNAME` | Usuario de la base de datos | — | Sí |
| `DB_PASSWORD` | Contraseña de la base de datos | — | Sí |
| `JWT_SECRET` | Secreto para firmar JWT (mín. 32 chars) | — | **Sí** |
| `JWT_EXPIRATION_MINUTES` | Duración del token en minutos | `480` | No |
| `CORS_ORIGINS` | Origen permitido para CORS | `http://localhost:4200` | No |
| `AUTH_COOKIE_SECURE` | Enviar cookie solo por HTTPS | `false` | No |
| `AUTH_COOKIE_SAME_SITE` | Política SameSite de la cookie | `Strict` | No |
| `APP_TIME_ZONE` | Zona horaria de la aplicación | `America/Bogota` | No |
| `MAIL_HOST` | Host del servidor SMTP | `localhost` | No |
| `MAIL_PORT` | Puerto SMTP | `1025` | No |
| `MAIL_USERNAME` | Usuario SMTP | — | No |
| `MAIL_PASSWORD` | Contraseña SMTP | — | No |
| `MAIL_FROM` | Dirección de remitente | `no-reply@inventario.local` | No |
| `OPS_EMAIL` | Email del equipo de operaciones | `admin@inventario.local` | No |

> **Importante — JWT_SECRET:** El backend no arranca si `JWT_SECRET` está ausente, mide menos de 32 caracteres o usa el valor de ejemplo. Genera uno con:
> ```bash
> openssl rand -hex 32
> ```

> **Importante — AUTH_COOKIE_SAME_SITE:** Usa `Strict` cuando frontend y backend comparten origen (desarrollo local). Si se despliegan en dominios distintos, usa `None` junto con `AUTH_COOKIE_SECURE=true` (requiere HTTPS).

### URL del Backend (Frontend)

Edita `frontend/src/environments/environment.ts`:
```typescript
export const environment = {
  apiUrl: 'http://localhost:8080/api'  // Cambia para producción
};
```

---

## API Reference

La documentación completa e interactiva está disponible en Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

### Autenticación

| Método | Endpoint | Descripción |
|---|---|---|
| `POST` | `/api/auth/login` | Iniciar sesión → genera cookie JWT |
| `POST` | `/api/auth/logout` | Cerrar sesión → invalida cookie |
| `GET` | `/api/auth/session` | Verificar sesión activa |

### Productos

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/productos` | Todos | Listar con filtros: `nombre`, `categoria`, `stockBajo`, `activo`, `page`, `size` |
| `GET` | `/api/productos/{id}` | Todos | Detalle de producto |
| `GET` | `/api/productos/stock-bajo` | Todos | Productos por debajo del stock mínimo |
| `POST` | `/api/productos` | ADMIN | Crear producto |
| `PUT` | `/api/productos/{id}` | ADMIN | Editar producto |
| `DELETE` | `/api/productos/{id}` | ADMIN | Desactivar producto (soft delete) |
| `PATCH` | `/api/productos/{id}/reactivar` | ADMIN | Reactivar producto inactivo |
| `PATCH` | `/api/productos/{id}/ajustar-stock` | ADMIN, ALMACENISTA | Ajuste de stock con signo |

### Proveedores

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/proveedores` | Todos | Listar con filtros: `nombre`, `page`, `size` |
| `GET` | `/api/proveedores/{id}` | Todos | Detalle de proveedor |
| `POST` | `/api/proveedores` | ADMIN | Crear proveedor |
| `PUT` | `/api/proveedores/{id}` | ADMIN | Editar proveedor |
| `DELETE` | `/api/proveedores/{id}` | ADMIN | Eliminar proveedor |

### Precios

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/precios/historial` | Todos | Historial filtrado por `productoId`, `proveedorId` |
| `GET` | `/api/precios/ultimo` | Todos | Último precio registrado para una combinación proveedor-producto |
| `POST` | `/api/precios` | ADMIN, GERENTE | Registrar nuevo precio |

### Órdenes de Compra

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/ordenes` | Todos | Listar con filtros: `estado`, `proveedorId`, `page`, `size` |
| `GET` | `/api/ordenes/{id}` | Todos | Detalle con líneas de la orden |
| `POST` | `/api/ordenes` | ADMIN, GERENTE | Crear orden en estado BORRADOR |
| `PUT` | `/api/ordenes/{id}/enviar` | ADMIN, GERENTE | Cambiar estado a ENVIADA |
| `POST` | `/api/ordenes/{id}/recepcion` | ADMIN, ALMACENISTA | Registrar recepción (actualiza stock) |
| `PATCH` | `/api/ordenes/{id}/cancelar` | ADMIN, GERENTE | Cancelar orden |

### Movimientos

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/movimientos` | Todos | Listar con filtros: `productoId`, `tipoMovimiento`, `fechaDesde`, `fechaHasta` |
| `POST` | `/api/entradas` | ADMIN, ALMACENISTA | Registrar entrada de stock |
| `POST` | `/api/salidas` | ADMIN, ALMACENISTA | Registrar salida de stock |

### Usuarios

| Método | Endpoint | Acceso | Descripción |
|---|---|---|---|
| `GET` | `/api/usuarios` | ADMIN | Listar usuarios con paginación |
| `POST` | `/api/usuarios` | ADMIN | Crear usuario con rol asignado |
| `PATCH` | `/api/usuarios/{id}/activo` | ADMIN, GERENTE | Activar o desactivar usuario |

### Dashboard

| Método | Endpoint | Descripción |
|---|---|---|
| `GET` | `/api/dashboard` | KPIs básicos |
| `GET` | `/api/dashboard/resumen` | Datos completos: KPIs, gráficos, actividad reciente |

---

## Control de Acceso

### Roles

| Rol | Descripción |
|---|---|
| `ADMIN` | Acceso completo. Gestiona usuarios, productos, proveedores y toda la operación. |
| `GERENTE` | Compras: proveedores, precios, crear/enviar órdenes. Consulta de productos. Dashboard. |
| `ALMACENISTA` | Operación: entradas, salidas, ajustes de stock, recepción de órdenes. Dashboard. |

### Matriz de Permisos por Módulo

| Módulo | ADMIN | GERENTE | ALMACENISTA |
|---|:---:|:---:|:---:|
| Dashboard | ✅ | ✅ | ✅ |
| Productos (consulta) | ✅ | ✅ | ✅ |
| Productos (crear/editar) | ✅ | ❌ | ❌ |
| Movimientos | ✅ | ✅ | ✅ |
| Entradas / Salidas | ✅ | ❌ | ✅ |
| Ajuste de stock | ✅ | ❌ | ✅ |
| Proveedores | ✅ | ✅ | ❌ |
| Precios | ✅ | ✅ | ❌ |
| Órdenes (crear/enviar) | ✅ | ✅ | ❌ |
| Órdenes (recepción) | ✅ | ❌ | ✅ |
| Usuarios | ✅ | ❌ | ❌ |

---

## Flujo de Uso

### Primer inicio de sesión

1. Entrar a `http://localhost:4200`
2. Iniciar sesión con `admin@inventario.local` / `password`
3. Ir a **Usuarios** y crear los usuarios del equipo con su rol correspondiente

### Configurar inventario inicial

1. En **Productos**: crear el catálogo con nombre, código, categoría y stock mínimo
2. En **Movimientos > Entradas**: registrar el stock inicial de cada producto
3. En **Proveedores**: registrar los proveedores activos (con RUC/NIT)
4. En **Precios**: registrar el precio de cada combinación proveedor-producto

### Ciclo de compra

```
Gerente crea orden (BORRADOR)
    → define proveedor + productos + cantidades
    → el sistema sugiere el último precio registrado

Gerente envía orden (ENVIADA)
    → notificación interna

Almacenista recibe mercancía (RECIBIDA / RECIBIDA_PARCIAL)
    → el stock se actualiza automáticamente
    → se genera un MovimientoInventario por cada producto recibido
```

### Atajos de teclado

| Atajo | Acción |
|---|---|
| `?` | Abrir panel de ayuda con todos los atajos |
| `/` | Enfocar el primer filtro de la tabla actual |
| `g d` | Ir a Dashboard |
| `g p` | Ir a Productos |
| `g m` | Ir a Movimientos |
| `g s` | Ir a Proveedores |
| `g r` | Ir a Precios |
| `g o` | Ir a Órdenes |
| `g u` | Ir a Usuarios |
| `Esc` | Cerrar diálogo activo |

---

## Reglas de Negocio

- El stock actual se almacena en `Producto.cantidadStock`.
- **Todo** cambio operativo de stock genera un `MovimientoInventario` con el usuario responsable.
- La edición de un producto no permite cambiar el stock directamente después de la creación inicial.
- Los ajustes usan cantidad con signo: positiva suma, negativa resta.
- Un ajuste no puede ser cero ni dejar el stock en negativo.
- Productos y proveedores **inactivos** no aparecen en listados operativos ni pueden usarse en operaciones nuevas.

---

## Contribuir

Ver [CONTRIBUTING.md](CONTRIBUTING.md) para la guía completa de contribución.

**Flujo rápido:**
1. Fork del repositorio
2. Crear rama: `git checkout -b feat/mi-funcionalidad`
3. Commit: `git commit -m "feat: descripción"`
4. Push: `git push origin feat/mi-funcionalidad`
5. Abrir Pull Request

---

## Licencia

Distribuido bajo la licencia MIT. Ver [LICENSE](LICENSE) para más información.
