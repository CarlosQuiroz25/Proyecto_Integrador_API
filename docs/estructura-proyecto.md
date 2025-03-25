# Estructura del Proyecto

## Directorios Principales
```
BaseF/
├── .mvn/               # Configuración de Maven Wrapper
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── cesde/
│   │   │           └── proyecto_integrador/
│   │   └── resources/
│   └── test/
├── docs/
│   ├── entidades/
│   ├── configuracion-entorno.md
│   ├── docker-configuracion.md
│   ├── estructura-proyecto.md
│   └── Exportar json OpenAPI.md
├── .dockerignore
├── .env
├── .gitattributes
├── .gitignore
├── Dockerfile
├── pom.xml
├── project.toml
└── README.md
```

## Dependencias Principales
- Spring Boot Starter Web
- Spring Data JPA
- MySQL Connector/J
- Spring Boot DevTools
- Spring Security
- JSON Web Token (JWT)
- SpringDoc OpenAPI
- Lombok
- PostgreSQL Driver
- Spring Boot Validation

## Archivos Clave
1. `pom.xml` - Gestión de dependencias y configuración de Maven
2. `Dockerfile` - Construcción de imagen Docker para despliegue
3. `src/main/resources/application.properties` - Configuración de Spring Boot

## Estructura de Código
- Patrón MVC (Modelo-Vista-Controlador)
- Paquetes organizados por:
  - `config`: Configuraciones de Spring (Cors, Seguridad, OpenAPI)
    - `config.data`: Configuraciones de acceso a datos
  - `controller`: Endpoints para autenticación y recursos
  - `dto`: Objetos de transferencia para entrada/salida de API
  - `exception`: Manejo personalizado de errores y respuestas
  - `model`: Entidades principales del dominio
  - `repository`: Interfaces JPA para acceso a datos
  - `security`: Implementación JWT y filtros de autenticación
  - `service`: Lógica de negocio e implementaciones
    - `service.impl`: Implementaciones concretas de servicios

## Dependencias Principales
- Spring Boot Starter Web
- Spring Data JPA
- MySQL Connector/J
- Spring Boot DevTools

## Configuraciones Especiales
- Swagger UI integrado en `/swagger-ui/index.html`
- Perfiles Maven para diferentes entornos
- Configuración Docker multi-etapa para producción