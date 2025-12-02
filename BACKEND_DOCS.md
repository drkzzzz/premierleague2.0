# Backend Premier League - Documentación Completa

## Descripción
Backend REST API para la aplicación Premier League, construido con Spring Boot 3.5.8, MySQL y Spring Security con JWT.

## Arquitectura

### Estructura del Proyecto
```
src/main/
├── java/com/premier/league/premier_backend/
│   ├── config/              # Configuración de seguridad y CORS
│   ├── controller/          # Controladores REST
│   ├── dto/                 # Data Transfer Objects
│   ├── exception/           # Excepciones personalizadas
│   ├── model/               # Entidades JPA
│   ├── repository/          # Interfaces JPA Repository
│   ├── security/            # Configuración JWT
│   ├── service/             # Lógica de negocio
│   └── PremierBackendApplication.java
├── resources/
│   ├── sql/
│   │   ├── premier_db.sql       # Esquema de la base de datos
│   │   └── premier_seeds.sql    # Datos iniciales
│   └── application.properties   # Configuración de la aplicación
```

## Base de Datos

### Tablas Principales

1. **roles** - Define los roles de usuarios (ROLE_USER, ROLE_ADMIN, etc.)
2. **usuarios** - Almacena usuarios con autenticación
3. **sesiones** - Gestiona refresh tokens
4. **permisos** - Define permisos granulares
5. **rol_permiso** - Relación muchos-a-muchos entre roles y permisos
6. **comentarios** - Comentarios en partidos, noticias y equipos
7. **favoritos_equipo** - Equipos favoritos de usuarios
8. **favoritos_jugador** - Jugadores favoritos de usuarios

## Modelos de Datos

### Usuario
```java
- idUsuario: Long (PK)
- idRol: Long (FK)
- nombreUsuario: String (UNIQUE)
- email: String (UNIQUE)
- passwordHash: String
- fechaRegistro: LocalDateTime
```

### Comentario
```java
- idComentario: Long (PK)
- idUsuario: Long (FK)
- contenido: Text
- fechaCreacion: LocalDateTime
- entidadId: String
- entidadTipo: ENUM(PARTIDO, NOTICIA, EQUIPO)
```

### Favorito Equipo
```java
- idFavorito: Long (PK)
- idUsuario: Long (FK)
- idEquipoApi: String
- fechaAdicion: LocalDateTime
```

### Favorito Jugador
```java
- idFavorito: Long (PK)
- idUsuario: Long (FK)
- idJugadorApi: String
- fechaAdicion: LocalDateTime
```

## Endpoints API

### Autenticación (`/api/auth`)

#### Registro
```http
POST /api/auth/registro
Content-Type: application/json

{
  "nombreUsuario": "usuario123",
  "email": "usuario@example.com",
  "password": "contraseña123"
}

Respuesta:
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "idUsuario": 1,
    "nombreUsuario": "usuario123",
    "email": "usuario@example.com"
  }
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "nombreUsuario": "usuario123",
  "password": "contraseña123"
}

Respuesta:
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "idUsuario": 1,
    "nombreUsuario": "usuario123",
    "email": "usuario@example.com"
  }
}
```

#### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGc..."
}

Respuesta:
{
  "success": true,
  "accessToken": "eyJhbGc..."
}
```

#### Logout
```http
POST /api/auth/logout
Content-Type: application/json

{
  "refreshToken": "eyJhbGc..."
}

Respuesta:
{
  "success": true,
  "message": "Logout exitoso"
}
```

### Usuarios (`/api/usuarios`)

#### Obtener Todos
```http
GET /api/usuarios
Authorization: Bearer <accessToken>

Respuesta:
{
  "success": true,
  "data": [
    {
      "idUsuario": 1,
      "idRol": 2,
      "nombreUsuario": "usuario123",
      "email": "usuario@example.com",
      "fechaRegistro": "2024-12-02T10:30:00"
    }
  ]
}
```

#### Obtener por ID
```http
GET /api/usuarios/{idUsuario}
Authorization: Bearer <accessToken>
```

#### Obtener por Email
```http
GET /api/usuarios/email/{email}
Authorization: Bearer <accessToken>
```

#### Actualizar Usuario
```http
PUT /api/usuarios/{idUsuario}
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "email": "newemail@example.com",
  "idRol": 2
}
```

#### Cambiar Contraseña
```http
POST /api/usuarios/{idUsuario}/cambiar-contrasena
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "contrasenaActual": "contraseña123",
  "contrasenaNueva": "nuevacontraseña123"
}
```

#### Eliminar Usuario
```http
DELETE /api/usuarios/{idUsuario}
Authorization: Bearer <accessToken>
```

### Roles (`/api/roles`)

#### Crear Rol
```http
POST /api/roles
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "nombre": "ROLE_ADMIN"
}
```

#### Obtener Todos los Roles
```http
GET /api/roles
Authorization: Bearer <accessToken>
```

#### Obtener Rol por ID
```http
GET /api/roles/{idRol}
Authorization: Bearer <accessToken>
```

#### Actualizar Rol
```http
PUT /api/roles/{idRol}
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "nombre": "ROLE_MODERATOR"
}
```

#### Eliminar Rol
```http
DELETE /api/roles/{idRol}
Authorization: Bearer <accessToken>
```

### Permisos (`/api/permisos`)

#### Crear Permiso
```http
POST /api/permisos
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "nombre": "gestion_usuarios"
}
```

#### Obtener Todos los Permisos
```http
GET /api/permisos
Authorization: Bearer <accessToken>
```

#### Obtener Permiso por ID
```http
GET /api/permisos/{idPermiso}
Authorization: Bearer <accessToken>
```

#### Actualizar Permiso
```http
PUT /api/permisos/{idPermiso}
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "nombre": "eliminar_comentarios"
}
```

#### Eliminar Permiso
```http
DELETE /api/permisos/{idPermiso}
Authorization: Bearer <accessToken>
```

### Comentarios (`/api/comentarios`)

#### Crear Comentario
```http
POST /api/comentarios
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "idUsuario": 1,
  "contenido": "Gran partido!",
  "entidadId": "partido123",
  "entidadTipo": "PARTIDO"
}
```

#### Obtener por Entidad
```http
GET /api/comentarios/entidad/{entidadId}/{entidadTipo}
Authorization: Bearer <accessToken>

Ejemplo: GET /api/comentarios/entidad/partido123/PARTIDO
```

#### Obtener por Usuario
```http
GET /api/comentarios/usuario/{idUsuario}
Authorization: Bearer <accessToken>
```

#### Obtener por ID
```http
GET /api/comentarios/{idComentario}
Authorization: Bearer <accessToken>
```

#### Eliminar Comentario
```http
DELETE /api/comentarios/{idComentario}
Authorization: Bearer <accessToken>
```

### Favoritos Equipos (`/api/favoritos/equipos`)

#### Agregar Favorito
```http
POST /api/favoritos/equipos
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "idUsuario": 1,
  "idEquipoApi": "133602"
}
```

#### Obtener Favoritos del Usuario
```http
GET /api/favoritos/equipos/usuario/{idUsuario}
Authorization: Bearer <accessToken>
```

#### Eliminar Favorito
```http
DELETE /api/favoritos/equipos
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "idUsuario": 1,
  "idEquipoApi": "133602"
}
```

### Favoritos Jugadores (`/api/favoritos/jugadores`)

#### Agregar Favorito
```http
POST /api/favoritos/jugadores
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "idUsuario": 1,
  "idJugadorApi": "123456"
}
```

#### Obtener Favoritos del Usuario
```http
GET /api/favoritos/jugadores/usuario/{idUsuario}
Authorization: Bearer <accessToken>
```

#### Eliminar Favorito
```http
DELETE /api/favoritos/jugadores
Authorization: Bearer <accessToken>
Content-Type: application/json

{
  "idUsuario": 1,
  "idJugadorApi": "123456"
}
```

### Health Check (`/api/health`)

#### Verificar Estado del Backend
```http
GET /api/health

Respuesta:
{
  "success": true,
  "status": "UP",
  "message": "Backend Premier League está en línea"
}
```

## Seguridad

### JWT Tokens
- **Access Token**: Válido por 24 horas (86400000ms)
- **Refresh Token**: Válido por 7 días (604800000ms)

### Contraseñas
- Se encriptan con BCryptPasswordEncoder
- Nunca se devuelven en las respuestas

### CORS
Configurado para aceptar solicitudes desde:
- http://localhost:3000
- http://localhost:5173
- Localhost en general

## Configuración de la Aplicación

### Properties Principales
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/premier_league_db
spring.datasource.username=root
spring.datasource.password=

jwt.secret=miSecretSuperSeguroParaLaPremierLeagueBackend2024
jwt.expiration=86400000
jwt.refresh-expiration=604800000

server.port=8080
```

## Requisitos Previos

1. **Java**: JDK 17+
2. **Maven**: 3.6+
3. **MySQL**: 5.7+
4. **XAMPP** (para desarrollo local)

## Instalación y Ejecución

### 1. Base de Datos
```sql
-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS premier_league_db;
USE premier_league_db;

-- El backend ejecutará automáticamente premier_db.sql y premier_seeds.sql
```

### 2. Compilar el Proyecto
```bash
cd premier-backend
mvn clean compile
```

### 3. Ejecutar la Aplicación
```bash
mvn spring-boot:run
```

O desde una IDE (IntelliJ, Eclipse, VS Code):
- Ejecutar `PremierBackendApplication.java`

### 4. Verificar que esté corriendo
```bash
curl http://localhost:8080/api/health
```

## Dependencias Principales

```xml
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- MySQL Connector/J
- JJWT (JWT)
- Lombok
```

## Estructura de Respuestas

### Respuesta Exitosa
```json
{
  "success": true,
  "message": "Operación completada exitosamente",
  "data": { /* datos */ }
}
```

### Respuesta con Error
```json
{
  "success": false,
  "message": "Descripción del error",
  "error": "TipoDeError"
}
```

## Códigos HTTP Utilizados

- **200**: OK - Operación exitosa
- **201**: Created - Recurso creado exitosamente
- **400**: Bad Request - Error en los parámetros
- **401**: Unauthorized - Sin autenticación o token inválido
- **404**: Not Found - Recurso no encontrado
- **409**: Conflict - Conflicto (ej: usuario ya existe)
- **500**: Internal Server Error - Error interno del servidor

## Próximos Pasos

1. Crear endpoints para integrar con API externa de Premier League
2. Implementar más filtros y búsquedas avanzadas
3. Agregar validaciones más rigurosas
4. Implementar rate limiting
5. Agregar logs más detallados
6. Crear tests unitarios

## Contacto y Soporte

Para problemas o preguntas sobre el backend, consultar la documentación de Spring Boot:
- https://spring.io/projects/spring-boot
