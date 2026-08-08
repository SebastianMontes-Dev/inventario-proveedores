<div align="center">
  <h1>📦 Inventario de Proveedores</h1>
  <p><strong>Plataforma Empresarial Integral para la Gestión de Proveedores</strong></p>
  
  <p>
    <img src="https://img.shields.io/badge/Estado-Activo-success?style=for-the-badge" alt="Estado del Proyecto" />
    <img src="https://img.shields.io/badge/Versi%C3%B3n-1.0.0-blue?style=for-the-badge" alt="Versión" />
    <img src="https://img.shields.io/badge/Licencia-MIT-green?style=for-the-badge" alt="Licencia" />
  </p>

  <p>
    <img src="https://img.shields.io/badge/Spring_Boot-3.5.14-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
    <img src="https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java 17" />
    <img src="https://img.shields.io/badge/Angular-17-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular 17" />
    <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
    <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  </p>
</div>

<hr />

## 📖 Descripción del Proyecto

**Inventario de Proveedores** es una solución tecnológica integral diseñada para simplificar y optimizar la administración del ciclo de vida de los proveedores de una organización. Con un enfoque en la escalabilidad, la seguridad y una experiencia de usuario moderna, este sistema centraliza todos los datos críticos y facilita el control riguroso del inventario.

---

## 🏗️ Arquitectura de la Solución

El sistema está estructurado mediante una arquitectura robusta de capas distribuidas:

- ⚙️ **Lado del Servidor (API RESTful):** Desarrollado en **Java 17** con el entorno de trabajo **Spring Boot**. Incorpora **Spring Security** y **JWT** para la autenticación segura, **Spring Data JPA** para la persistencia de datos y validaciones estrictas de información.
- 🎨 **Lado del Cliente (Aplicación de Página Única):** Construido sobre **Angular 17** utilizando **Angular Material** para asegurar una interfaz consistente, adaptable a múltiples dispositivos y altamente accesible.
- 🗄️ **Base de Datos:** **MySQL 8** gestionado y desplegado de manera automatizada a través de contenedores **Docker**.
- 📚 **Documentación de la API:** Generada automáticamente y de forma interactiva mediante **SpringDoc OpenAPI (Swagger)**.

---

## 🚀 Inicio Rápido

Desplegar el proyecto en un entorno local es un proceso sumamente sencillo gracias al uso de **Docker Compose**.

### 1️⃣ Requisitos Previos

Asegúrese de contar con las siguientes herramientas:
- 🐳 [Docker](https://www.docker.com/) y Docker Compose instalados en su sistema.
- ☕ **JDK 17** y 🟢 **Node.js (v18+)** instalados (únicamente si desea ejecutar la aplicación en modo de desarrollo nativo sin contenedores).

### 2️⃣ Inicializar la Base de Datos

Ejecute el siguiente comando en el directorio raíz del proyecto para iniciar el contenedor de MySQL con sus respectivos esquemas y datos iniciales:

```bash
docker-compose up -d
```

### 3️⃣ Iniciar el Lado del Servidor

Navegue al directorio `/backend` y ejecute:

```bash
cd backend
./mvnw spring-boot:run
```
> 💡 *Nota: El servidor se iniciará por defecto en `http://localhost:8080`. Puede consultar la documentación de Swagger en `http://localhost:8080/swagger-ui.html`.*

### 4️⃣ Iniciar el Lado del Cliente

Navegue al directorio `/frontend`, instale las dependencias necesarias e inicie el servidor de desarrollo:

```bash
cd frontend
npm install
npm start
```
> 💡 *Nota: Acceda a la plataforma desde su navegador en `http://localhost:4200`.*

---

## 🤝 Contribuciones

¡Todas las contribuciones son bienvenidas y valoradas! Por favor, revise el archivo [CONTRIBUTING.md](CONTRIBUTING.md) para conocer nuestros lineamientos de colaboración, o abra una *incidencia* para discutir cualquier mejora propuesta o reportar algún *error*.

---

## 📄 Licencia

Este proyecto se distribuye bajo los términos de la **Licencia MIT**. Para obtener información más detallada, consulte el archivo [LICENSE](LICENSE).

<div align="center">
  <sub>Construido con ❤️ para optimizar la gestión empresarial.</sub>
</div>
