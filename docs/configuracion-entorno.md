# Configuración del Entorno (.env)

## Variables Requeridas
```
# MySQL
DB_URL=jdbc:mysql://localhost:3306/data_pi?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=""

# JWT
JWT_SECRET=tu_secreto_super_seguro_aqui
```

## Mejores Prácticas de Seguridad
1. **Nunca commits el archivo .env** - Agrégalo a .gitignore
2. Usa secretos complejos (mínimo 32 caracteres, mezcla mayúsculas, números y símbolos)
3. Usa diferentes credenciales por entorno (desarrollo, producción)
4. Rotar los secretos JWT periódicamente
