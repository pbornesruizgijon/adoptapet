# AdoptAPet 🐾

**AdoptAPet** es una aplicación web completa desarrollada como proyecto académico para el ciclo formativo de **Desarrollo de Aplicaciones Web (DAW)**. La plataforma está diseñada para centralizar y facilitar el proceso de adopción de mascotas, conectando a usuarios con animales que buscan un hogar.

El proyecto implementa una arquitectura robusta en el backend, integración con APIs de terceros, seguridad avanzada y un flujo de despliegue moderno basado en contenedores y la nube.

---

## 🚀 Características Principales

* **Gestión Integral de Mascotas:** CRUD completo de animales disponibles para adopción (detalles, edad, especie, estado).
* **Seguridad y Roles:** Implementación de **Spring Security** para proteger rutas y gestionar roles de acceso (Usuarios / Administradores).
* **Autenticación Social (OAuth 2.0):** Permite el inicio de sesión rápido utilizando proveedores externos (como Google/GitHub).
* **Frontend Integrado con Thymeleaf:** Interfaz dinámica renderizada en el servidor mediante plantillas HTML5, CSS3 y componentes interactivos.
* **Almacenamiento en la Nube:** Integración con la API de **Cloudinary** para la subida y optimización de imágenes de las mascotas de forma dinámica.
* **Sistema de Notificaciones por Email:** Envío automático de correos electrónicos (confirmaciones de adopción, contacto) a través de **SpringMail**.
* **Documentación Automatizada:** * API documentada e interactiva con **Swagger / OpenAPI UI**.
    * Generación técnica de código mediante **JavaDoc**.

---

## 🛠️ Stack Tecnológico & Dependencias

### Backend & Core
* **Lenguaje:** Java 23
* **Framework:** Spring Boot 3.5.x
* **Seguridad:** Spring Security + OAuth2 Client
* **Motor de Plantillas:** Thymeleaf (con extensiones para Spring Security 6)

### Persistencia de Datos
* **ORM:** Spring Data JPA (Hibernate)
* **Base de Datos (Local):** MySQL (a través de `mysql-connector-j`)
* **Base de Datos (Producción):** PostgreSQL (configurada para el despliegue en Render)

### APIs de Terceros & Herramientas
* **Cloudinary:** Gestión y almacenamiento de archivos multimedia.
* **JavaMail Sender:** Servicio SMTP para el envío de correos.
* **Lombok:** Optimización de código (generación automática de Getters, Setters, Constructores).
* **Springdoc OpenAPI:** Generación de la interfaz Swagger para pruebas de endpoints.

---

## 🐳 Arquitectura de Despliegue

El proyecto ha sido diseñado siguiendo estándares modernos de DevOps, permitiendo dos entornos de ejecución bien diferenciados:

1.  **Entorno Local (Docker):** Contenedores listos para empaquetar la aplicación y la base de datos MySQL, asegurando que el proyecto funcione en cualquier máquina sin configuraciones previas.
2.  **Entorno de Producción (Render):** Despliegue automatizado en la nube de **Render** conectado a una base de datos gestionada **PostgreSQL**, ideal para la presentación final del proyecto.

---
