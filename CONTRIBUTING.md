# Guía de Contribución

¡Gracias por tu interés en contribuir al proyecto! Esta guía explica cómo configurar el entorno de desarrollo, las convenciones que seguimos y el flujo para enviar cambios.

---

## Tabla de Contenidos

- [Configuración del entorno](#configuración-del-entorno)
- [Estructura del monorepo](#estructura-del-monorepo)
- [Convenciones de código](#convenciones-de-código)
- [Flujo de trabajo Git](#flujo-de-trabajo-git)
- [Ejecutar tests](#ejecutar-tests)
- [Checklist antes de un PR](#checklist-antes-de-un-pr)

---

## Configuración del entorno

### Requisitos

- **Java 17+** (se recomienda SDKMAN o IntelliJ JDK manager)
- **Maven 3.9+**
- **Node.js 20+** (se recomienda nvm)
- **Docker 20+** (para la base de datos)
- **Git 2.30+**

### Pasos

```bash
# 1. Fork y clonar
git clone https://github.com/<tu-usuario>/sistema-inventario-proveedores.git
cd sistema-inventario-proveedores

# 2. Crear rama de trabajo
git checkout -b feat/mi-funcionalidad

# 3. Levantar MySQL
docker compose up -d

# 4. Importar esquema
mysql -h 127.0.0.1 -P 3307 -u inventario -pinventario123 < db/schema.sql
mysql -h 127.0.0.1 -P 3307 -u inventario -pinventario123 < db/data.sql

# 5. Configurar variables del backend
cp backend/.env.example backend/.env
# Editar backend/.env — al menos JWT_SECRET (mín. 32 chars)

# 6. Iniciar backend
cd backend && mvn spring-boot:run

# 7. Instalar dependencias e iniciar frontend (en otra terminal)
cd frontend && npm install && npm start
```

---

## Estructura del monorepo

```
backend/    → API REST Spring Boot  (Java 17 / Maven)
frontend/   → SPA Angular 17       (TypeScript / npm)
db/         → Scripts SQL           (schema + datos seed)
docs/       → Documentación técnica adicional
```

Cada sub-proyecto es independiente y tiene sus propios scripts de build/test.

---

## Convenciones de código

### Backend (Java / Spring Boot)

- Nombrado en **camelCase** para métodos y variables; **PascalCase** para clases.
- DTOs separados de las entidades JPA — nunca devolver entidades directamente en la API.
- Toda operación que cambie stock **debe** crear un `MovimientoInventario`.
- Usar `@PreAuthorize` en los controladores para controlar acceso por rol.
- Excepciones con mensaje claro y status HTTP apropiado via `@ResponseStatus` o `ResponseEntity`.
- Tests en `src/test/java` con JUnit 5 y `@SpringBootTest` para integración.

### Frontend (Angular / TypeScript)

- Componentes **standalone** — no usar NgModules salvo necesidad real.
- **Reactive Forms** para todos los formularios.
- Los servicios en `core/` son globales (providedIn: 'root'); los de `shared/` también.
- Las interfaces y tipos van en `core/models.ts`.
- Estilos en SCSS usando los **design tokens** de `styles/_tokens.scss` — no usar valores mágicos de color, espaciado o tipografía directamente.
- Lazy loading para todas las rutas de `features/`.
- Nunca subscribearse en el constructor — usar `ngOnInit` o señales.
- Cancelar todas las suscripciones RxJS en `ngOnDestroy` (o usar `takeUntilDestroyed`).

### Mensajes de commit

Seguimos **Conventional Commits**:

```
<tipo>(<ámbito>): <descripción corta en imperativo>

[cuerpo opcional]

[pie opcional]
```

**Tipos:**

| Tipo | Cuándo usarlo |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de bug |
| `refactor` | Refactoring sin cambio de comportamiento |
| `style` | Cambios de formato / estilos (no lógica) |
| `test` | Agregar o modificar tests |
| `docs` | Cambios en documentación |
| `chore` | Tareas de mantenimiento (deps, config) |
| `perf` | Mejoras de rendimiento |

**Ejemplos:**
```
feat(ordenes): agregar filtro por rango de fechas
fix(auth): corregir expiración de cookie en Safari
docs(readme): actualizar sección de configuración
refactor(productos): extraer validación a método privado
```

---

## Flujo de trabajo Git

```
main                  ← rama de producción (protegida)
  └─ feat/<nombre>    ← funcionalidades nuevas
  └─ fix/<nombre>     ← correcciones de bugs
  └─ docs/<nombre>    ← documentación
  └─ refactor/<nombre>← refactoring
```

### Pasos para enviar cambios

```bash
# 1. Asegurarse de estar actualizado con main
git fetch origin
git rebase origin/main

# 2. Hacer los cambios y commitear
git add <archivos>
git commit -m "feat(módulo): descripción"

# 3. Push de la rama
git push origin feat/mi-funcionalidad

# 4. Abrir Pull Request en GitHub
#    → Usar el template de PR (si existe)
#    → Asignar reviewers
#    → Vincular issue relacionado (#número)
```

---

## Ejecutar tests

### Backend

```bash
cd backend
mvn test                          # Todos los tests
mvn test -pl . -Dtest=NombreTest  # Un test específico
mvn verify                        # Tests + verificación de build
```

### Frontend

```bash
cd frontend
npm test                          # Jest en modo watch
npm test -- --coverage            # Con reporte de cobertura
npm test -- --watchAll=false      # Una sola ejecución (CI)
```

### Verificar build de producción

```bash
# Backend
cd backend && mvn clean package -DskipTests

# Frontend
cd frontend && npm run build
```

---

## Checklist antes de un PR

Antes de solicitar revisión, verifica que:

- [ ] El código compila sin errores (`mvn clean package` / `npm run build`)
- [ ] Los tests existentes pasan (`mvn test` / `npm test -- --watchAll=false`)
- [ ] Si agregaste funcionalidad nueva, incluiste tests
- [ ] El mensaje de commit sigue Conventional Commits
- [ ] No hay `console.log` de depuración olvidados en el frontend
- [ ] No hay credenciales ni secretos en el código
- [ ] Variables de entorno nuevas están documentadas en `.env.example` y en el README
- [ ] Los cambios de UI se ven bien en mobile (360px) y desktop (1280px+)
- [ ] Los cambios de UI respetan Dark Mode

---

## Reportar bugs

Usar el [issue tracker de GitHub](../../issues) con:

1. **Descripción clara** del problema
2. **Pasos para reproducir** (paso a paso)
3. **Comportamiento esperado** vs **comportamiento actual**
4. **Entorno**: OS, versión del browser, versión de Java/Node
5. **Logs relevantes** (consola del browser, logs del backend)

---

## Proponer nuevas funcionalidades

Abrir un issue con el label `enhancement` describiendo:

1. El problema que resuelve
2. La solución propuesta
3. Alternativas consideradas
4. Impacto en roles existentes (si aplica)
