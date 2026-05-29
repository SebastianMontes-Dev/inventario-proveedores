# Changelog

All notable changes to this project are documented here.

---

## [1.0.0] — 2025-05

### Added

**Backend (Spring Boot 3.5)**
- Full REST API with 11 controllers: products, suppliers, prices, purchase orders, stock movements, users, auth, dashboard
- JWT authentication stored in `HttpOnly` cookies — not accessible from JavaScript
- Role-based access control with `@PreAuthorize`: `ADMIN`, `GERENTE`, `ALMACENISTA`
- Complete purchase order lifecycle: `BORRADOR → ENVIADA → RECIBIDA_PARCIAL → RECIBIDA / CANCELADA`
- Stock auto-update on order reception with audit trail (`MovimientoInventario`)
- `GET /api/dashboard/resumen` — aggregated KPIs computed server-side to avoid pagination-dependent client calculations
- `GET /api/precios/ultimo` — last known price per supplier-product pair, used to prefill order forms
- `GET /api/auth/session` — session revalidation endpoint used on app startup to sync `localStorage` state with live cookie
- Configurable timezone via `APP_TIME_ZONE` env var (default `America/Bogota`) for date-range filters
- Email notifications via Thymeleaf templates: welcome email, low-stock alert, order dispatched
- Swagger UI via Springdoc OpenAPI at `/swagger-ui.html`
- 24 unit and integration tests (JUnit 5)

**Security hardening**
- JWT secret validated at startup: rejects values under 32 characters or known placeholder strings
- Pessimistic locking (`PESSIMISTIC_WRITE`) on order state transitions to prevent race conditions
- `GlobalExceptionHandler` returns generic messages to clients, logs full stack server-side
- `@Transactional` on all write operations in service layer
- `AuthService.create()` no longer sends plain-text passwords in welcome emails
- Configurable `SameSite` cookie policy (`Strict` for same-origin, `None` for cross-origin + HTTPS)
- `errorInterceptor` differentiates `401` (expired session) from `403` (insufficient permissions) — no longer signs the user out on permission errors

**Frontend (Angular 17)**
- Standalone components with lazy-loaded feature routes
- Angular Material design system with custom CSS tokens for Dark / Light mode toggle
- Dashboard with real-time KPIs, Chart.js movement distribution graph, top-activity products, low-stock list, and recent activity feed
- Reactive forms for all CRUD operations
- `APP_INITIALIZER` that revalidates the session via `/auth/session` before the router activates — prevents stale `localStorage` from letting unauthenticated users through
- Global keyboard shortcuts (`?` for help, `g d/p/m/s/r/o/u` for navigation, `Esc` to close dialogs)
- Skeleton loaders during data fetches
- Fully responsive layout (360 px → 1920 px)
- Accessibility: skip links, visible focus rings, `prefers-reduced-motion` support
- Navigation items hidden per role (e.g. suppliers and prices hidden for `ALMACENISTA`)

**Infrastructure**
- Docker Compose for local MySQL 8 database
- GitHub Actions CI: backend build + tests on every PR, frontend build check on every PR
- `.env.example` with all required and optional environment variables documented

### Fixed

- Cascade-to-login bug caused by `localStorage` / `HttpOnly` cookie desync on backend restart
- Dashboard 401 cascade: `AuthService.logout()` is now idempotent; `errorInterceptor` skips logout for `/auth/login` and `/auth/logout` requests
- Stock adjustments now reject zero-quantity changes and changes that would leave stock negative
- Product `update()` no longer silently modifies the `active` flag — only `create()` sets it
- Inactive products and suppliers are excluded from all operational listings and rejected in new operations
- Order form `unknown` TypeScript payload replaced with explicit `OrdenRequest` / `DetalleOrdenRequest` types

---

## Legend

- **Added** — new features
- **Fixed** — bug fixes
- **Changed** — changes to existing behavior
- **Removed** — removed features
