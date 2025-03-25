# Configuración de Docker para el Proyecto

## Índice
1. [Introducción](#introducción)
2. [Estructura del Dockerfile](#estructura-del-dockerfile)
3. [Variables de Entorno](#variables-de-entorno)
4. [Construcción y Despliegue](#construcción-y-despliegue)
5. [Buenas Prácticas](#buenas-prácticas)

## Introducción
Este documento describe la configuración de Docker para nuestro proyecto Spring Boot. La containerización nos permite garantizar la consistencia del entorno de ejecución y facilitar el despliegue de la aplicación.

## Estructura del Dockerfile
Nuestro Dockerfile utiliza un enfoque de construcción multi-etapa para optimizar el tamaño de la imagen final:

```dockerfile
FROM maven:3.8.4-openjdk-17 as builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
```

### Explicación de las Etapas

1. **Etapa de Construcción (builder)**
   - Utiliza la imagen de Maven con OpenJDK 17
   - Copia el código fuente al contenedor
   - Compila la aplicación y genera el archivo JAR

2. **Etapa Final**
   - Usa una imagen base más ligera con OpenJDK 17
   - Copia solo el archivo JAR de la etapa anterior
   - Configura el punto de entrada para ejecutar la aplicación

## Variables de Entorno
Las variables de entorno se gestionan a través del archivo `.env`:

```env
SPRING_PROFILES_ACTIVE=prod
DB_HOST=localhost
DB_PORT=5432
DB_NAME=proyecto_integrador
DB_USERNAME=usuario
DB_PASSWORD=contraseña
```

## Construcción y Despliegue

### Construir la Imagen
```bash
docker build -t proyecto-integrador .
```

### Ejecutar el Contenedor
```bash
docker run -d \
  --name proyecto-integrador \
  -p 8080:8080 \
  --env-file .env \
  proyecto-integrador
```

