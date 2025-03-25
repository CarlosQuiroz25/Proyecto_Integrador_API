# Proyecto Integrador

## Descripción
Este proyecto es una API RESTful desarrollada con Spring Boot que implementa un sistema de gestión con autenticación y autorización basada en roles. La API está diseñada para ser desplegada en contenedores Docker y puede ser ejecutada tanto en entornos locales como en la nube (Google Cloud Run o Render.com).

## Tecnologías Utilizadas
- Java 21
- Spring Boot 3.4.3
- Spring Security
- Spring Data JPA
- MySQL / PostgreSQL
- JWT (JSON Web Tokens)
- Maven
- Docker
- Swagger / OpenAPI

## Requisitos Previos
- JDK 21
- Maven 3.8+
- MySQL o PostgreSQL
- Docker (opcional, para despliegue en contenedores)

## Estructura del Proyecto

```
.
├── .mvn/                  # Configuración de Maven Wrapper
├── docs/                  # Documentación del proyecto
├── src/                   # Código fuente
│   ├── main/
│   │   ├── java/         
│   │   │   └── com/cesde/proyecto_integrador/
│   │   │       ├── config/       # Configuraciones Spring
│   │   │       │   └── data/     # Config acceso a datos
│   │   │       ├── controller/   # Endpoints API REST
│   │   │       ├── dto/          # Objetos transferencia
│   │   │       ├── exception/    # Manejo de errores
│   │   │       ├── model/        # Entidades dominio
│   │   │       ├── repository/   # Interfaces JPA
│   │   │       ├── security/     # Autenticación JWT
│   │   │       └── service/      # Lógica de negocio
│   │   │           └── impl/     # Implementaciones
│   │   └── resources/     # Recursos y configuraciones
│   └── test/
│       └── java/         # Pruebas unitarias
├── .dockerignore          
├── .env                   
├── .gitattributes         
├── .gitignore             
├── Dockerfile             
├── mvnw                   
├── mvnw.cmd               
├── pom.xml                
├── project.toml           
└── README.md              
```

## Configuración del Entorno

### Archivo .env
Crea un archivo `.env` en la raíz del proyecto con las siguientes variables:

```
# MySQL
DB_URL=jdbc:mysql://localhost:3306/data_pi
DB_USERNAME=root
DB_PASSWORD=tu_contraseña

# JWT
JWT_SECRET=tu_secreto_super_seguro_aqui
```

### Recomendaciones de Seguridad
1. **Nunca subas el archivo .env al repositorio** - Asegúrate de que esté incluido en `.gitignore`
2. Usa secretos complejos para JWT (mínimo 32 caracteres, mezcla mayúsculas, números y símbolos)
3. Usa diferentes credenciales por entorno (desarrollo, producción)
4. Rota los secretos JWT periódicamente

## Instalación y Ejecución

### Ejecución Local con Maven

1. Clona el repositorio:
   ```bash
   git clone [URL_DEL_REPOSITORIO]
   cd BaseV2
   ```

2. Crea y configura el archivo `.env` como se indicó anteriormente.

3. Compila el proyecto:
   ```bash
   ./mvnw clean package
   ```

4. Ejecuta la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```

5. La API estará disponible en: http://localhost:8080

### Ejecución con Docker

1. Construye la imagen Docker:
   ```bash
   docker build -t proyecto-integrador .
   ```

2. Ejecuta el contenedor:
   ```bash
   docker run -d \
     --name proyecto-integrador \
     -p 8080:8080 \
     --env-file .env \
     proyecto-integrador
   ```

3. La API estará disponible en: http://localhost:8080

## Documentación de la API

La documentación de la API está disponible a través de Swagger UI:

- URL: http://localhost:8080/swagger-ui/index.html
- JSON: http://localhost:8080/v3/api-docs

## Estructura de Código Fuente

### Paquetes Principales
```
com.cesde.proyecto_integrador/
├── config/                # Configuraciones (CORS, OpenAPI, Security)
│   └── data/             # Carga inicial de datos
├── controller/            # Controladores REST por rol
├── dto/                   # DTOs para usuarios y categorías
├── exception/             # Manejo global de excepciones
├── model/                 # Entidades (User, Profile)
├── repository/            # Repositorios JPA
├── security/              # Configuración JWT
├── service/               # Servicios por rol de usuario
│   └── impl/             # Implementaciones de servicios
└── ProyectoIntegradorApplication.java
```

## Autenticación y Autorización

### Endpoints de Autenticación

- **Login**: `POST /api/auth/login`
  - Cuerpo de la solicitud:
    ```json
    {
      "email": "usuario@ejemplo.com",
      "password": "contraseña"
    }
    ```
  - Respuesta:
    ```json
    {
      "token": "jwt_token",
      "username": "usuario@ejemplo.com",
      "role": "ADMIN"
    }
    ```

### Roles de Usuario

El sistema implementa tres roles principales:

- **ADMIN**: Acceso completo al sistema, incluida la gestión de usuarios
- **STUDENT**: Acceso a funcionalidades específicas para estudiantes
- **TEACHER**: Acceso a funcionalidades específicas para profesores

## Base de Datos

### Configuración

El proyecto está configurado para trabajar con MySQL por defecto, pero también soporta PostgreSQL. La configuración se encuentra en `application.properties`.

### Entidades Principales

#### User

- **Atributos**: id, email, password, role, createdAt, updatedAt
- **Roles**: ADMIN, STUDENT, TEACHER
- **Relaciones**: OneToOne con Profile

#### Profile

- **Atributos**: id, name, lastName, phone, address, urlPhoto
- **Relaciones**: OneToOne con User

## Despliegue en la Nube

### Google Cloud Run

El proyecto incluye configuración para ser desplegado en Google Cloud Run:

1. Construye la imagen Docker:
   ```bash
   docker build -t gcr.io/[PROJECT_ID]/proyecto-integrador .
   ```

2. Sube la imagen a Google Container Registry:
   ```bash
   docker push gcr.io/[PROJECT_ID]/proyecto-integrador
   ```

3. Despliega en Cloud Run:
   ```bash
   gcloud run deploy proyecto-integrador \
     --image gcr.io/[PROJECT_ID]/proyecto-integrador \
     --platform managed \
     --allow-unauthenticated
   ```

### Render.com

El proyecto también puede ser desplegado en Render.com utilizando la configuración alternativa en el Dockerfile.

## Documentación Adicional

Para más información, consulta los siguientes documentos en la carpeta `docs/`:

- [Estructura del Proyecto](docs/estructura-proyecto.md)
- [Configuración del Entorno](docs/configuracion-entorno.md)
- [Configuración de Docker](docs/docker-configuracion.md)
- [Entidad Usuario](docs/entidades/User.md)

## Dependencias del Proyecto

A continuación se detallan las dependencias utilizadas en el proyecto, organizadas por categorías:

### Core de Spring Boot

- **spring-boot-starter-data-jpa**: Proporciona soporte para JPA (Java Persistence API), facilitando el acceso y la manipulación de bases de datos relacionales mediante ORM.
- **spring-boot-starter-web**: Incluye todo lo necesario para desarrollar aplicaciones web, incluyendo RESTful, usando Spring MVC y Apache Tomcat como servidor embebido.
- **spring-boot-devtools**: Herramientas para el desarrollo que permiten la recarga automática de la aplicación cuando se detectan cambios en el código.
- **spring-boot-starter-test**: Proporciona dependencias para pruebas unitarias e integración, incluyendo JUnit, Mockito y Spring Test.
- **spring-boot-starter-validation**: Ofrece validación de datos mediante la implementación de Bean Validation con Hibernate Validator.

### Seguridad

- **spring-boot-starter-security**: Implementa características de seguridad, autenticación y autorización en la aplicación.
- **jjwt (0.9.1)**: Biblioteca para crear y validar JSON Web Tokens (JWT), utilizada para la autenticación basada en tokens.
- **jaxb-api (2.3.1)** y **jaxb-runtime (2.3.1)**: Necesarios para el procesamiento XML en Java 9+, requeridos por JJWT para su funcionamiento correcto.

### Bases de Datos

- **mysql-connector-j**: Driver JDBC para conectar la aplicación con bases de datos MySQL.
- **postgresql**: Driver JDBC para conectar la aplicación con bases de datos PostgreSQL.

### Documentación de API

- **springdoc-openapi-starter-webmvc-ui (2.8.5)**: Genera automáticamente documentación OpenAPI 3 para APIs REST y proporciona Swagger UI para visualizar e interactuar con la API.

### Utilidades

- **lombok**: Reduce el código repetitivo (getters, setters, constructores, etc.) mediante anotaciones, mejorando la legibilidad y mantenibilidad del código.

Estas dependencias conforman el ecosistema tecnológico del proyecto, proporcionando las herramientas necesarias para el desarrollo de una API RESTful robusta, segura y bien documentada.

