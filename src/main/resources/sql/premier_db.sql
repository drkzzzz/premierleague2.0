-- src/main/resources/sql/premier_db.sql
-- Script de Creación de Tablas (Esquema)

-- Asegura que la BD exista y la usa. Si la usas en XAMPP, puedes crearla manualmente una vez.
-- CREATE DATABASE IF NOT EXISTS `premier_league_db`;
-- USE `premier_league_db`;

-- 1. Tabla de Roles
CREATE TABLE `roles` (
    `id_rol` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(50) NOT NULL UNIQUE, -- EJ: 'ROLE_USER', 'ROLE_ADMIN'
    PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Tabla de Usuarios (Autenticación y Perfil)
CREATE TABLE `usuarios` (
    `id_usuario` BIGINT NOT NULL AUTO_INCREMENT,
    `id_rol` BIGINT NOT NULL, -- Clave Foránea a roles
    `nombre_usuario` VARCHAR(50) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `password_hash` VARCHAR(255) NOT NULL,
    `fecha_registro` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id_usuario`),
    FOREIGN KEY (`id_rol`) REFERENCES `roles`(`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. Tabla de Sesiones (Seguridad de Refresh Tokens)
CREATE TABLE `sesiones` (
    `id_sesion` BIGINT NOT NULL AUTO_INCREMENT,
    `id_usuario` BIGINT NOT NULL,
    `token_refresh` VARCHAR(255) NOT NULL UNIQUE,
    `fecha_expiracion` DATETIME NOT NULL,
    `ip_acceso` VARCHAR(45),
    PRIMARY KEY (`id_sesion`),
    FOREIGN KEY (`id_usuario`) REFERENCES `usuarios`(`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. Tabla de Permisos (acciones granulares)
CREATE TABLE `permisos` (
    `id_permiso` BIGINT NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(100) NOT NULL UNIQUE, -- EJ: 'gestion_usuarios', 'eliminar_comentarios'
    PRIMARY KEY (`id_permiso`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. Tabla de Unión (Roles vs Permisos)
CREATE TABLE `rol_permiso` (
    `id_rol` BIGINT NOT NULL,
    `id_permiso` BIGINT NOT NULL,
    PRIMARY KEY (`id_rol`, `id_permiso`),
    FOREIGN KEY (`id_rol`) REFERENCES `roles`(`id_rol`) ON DELETE CASCADE,
    FOREIGN KEY (`id_permiso`) REFERENCES `permisos`(`id_permiso`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. Tabla de Comentarios
CREATE TABLE `comentarios` (
    `id_comentario` BIGINT NOT NULL AUTO_INCREMENT,
    `id_usuario` BIGINT NOT NULL,
    `contenido` TEXT NOT NULL,
    `fecha_creacion` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `entidad_id` VARCHAR(50) NOT NULL, 
    `entidad_tipo` ENUM('PARTIDO', 'NOTICIA', 'EQUIPO') NOT NULL, 
    PRIMARY KEY (`id_comentario`),
    FOREIGN KEY (`id_usuario`) REFERENCES `usuarios`(`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. Tabla de Equipos Favoritos
CREATE TABLE `favoritos_equipo` (
    `id_favorito` BIGINT NOT NULL AUTO_INCREMENT,
    `id_usuario` BIGINT NOT NULL,
    `id_equipo_api` VARCHAR(20) NOT NULL, 
    `fecha_adicion` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id_favorito`),
    FOREIGN KEY (`id_usuario`) REFERENCES `usuarios`(`id_usuario`) ON DELETE CASCADE,
    UNIQUE KEY `uk_usuario_equipo` (`id_usuario`, `id_equipo_api`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. Tabla de Jugadores Favoritos
CREATE TABLE `favoritos_jugador` (
    `id_favorito` BIGINT NOT NULL AUTO_INCREMENT,
    `id_usuario` BIGINT NOT NULL,
    `id_jugador_api` VARCHAR(20) NOT NULL,
    `fecha_adicion` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id_favorito`),
    FOREIGN KEY (`id_usuario`) REFERENCES `usuarios`(`id_usuario`) ON DELETE CASCADE,
    UNIQUE KEY `uk_usuario_jugador` (`id_usuario`, `id_jugador_api`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;