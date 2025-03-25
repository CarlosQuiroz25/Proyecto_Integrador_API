# Obtener el archivo JSON de Swagger
- **Accede a la URL de Swagger JSON**:
  - En tu aplicación Spring Boot con `springdoc-openapi`, la especificación OpenAPI está disponible en: `http://localhost:8080/v3/api-docs` (o el puerto que estés usando).
  - Abre esta URL en un navegador o usa una herramienta como `curl`:
    ```bash
    curl http://localhost:8080/v3/api-docs -o swagger.json
    ```
  - Esto descargará un archivo `swagger.json` con la definición de tu API (endpoints, parámetros, etc.).

# Open API Documentación:

- Swagger UI: http://localhost:8080/swagger-ui/index.html;
- JSON de OpenAPI: http://localhost:8080/v3/api-docs;
- YAML de OpenAPI: http://localhost:8080/api-docs.yaml;