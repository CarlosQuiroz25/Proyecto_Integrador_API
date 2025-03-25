# Google cloud run

# Etapa 1: Construcción de la aplicación
FROM eclipse-temurin:21-jdk-alpine AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven y descargar dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y construir la aplicación
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Etapa 2: Imagen final para ejecutar la aplicación
FROM eclipse-temurin:21-jre-alpine

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=builder /app/target/proyecto-integrador-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que usa la aplicación (Cloud Run lo usa como referencia)
EXPOSE 8080

# Configurar el comando para ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]





# Render.com

# # Use official OpenJDK image as the base image
# FROM eclipse-temurin:21-jdk-alpine

# # Set working directory
# WORKDIR /app

# # Copy the Maven wrapper files
# COPY .mvn/ .mvn/
# COPY mvnw pom.xml ./

# # Set execute permissions for Maven wrapper
# RUN chmod +x mvnw

# # Download dependencies
# RUN ./mvnw dependency:go-offline

# # Copy the source code
# COPY src ./src

# # Build the application
# RUN ./mvnw clean package -DskipTests

# # Expose the port the app runs on
# EXPOSE 8080

# # Run the application
# CMD ["java", "-jar", "target/proyecto-integrador-0.0.1-SNAPSHOT.jar"]