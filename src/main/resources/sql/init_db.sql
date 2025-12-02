-- Script de inicialización para la Base de Datos
-- Archivo: src/main/resources/sql/init_db.sql

-- 1. Crear la base de datos si no existe.
-- Esto asegura que la base de datos esté disponible antes de que Hibernate intente crear las tablas dentro de ella.
-- El caracter '`' es la forma correcta de citar nombres de BD/Tabla en MySQL.

CREATE DATABASE IF NOT EXISTS `premier_league_db`
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_0900_ai_ci;

-- 2. Usar la base de datos.
-- Aunque la configuración de Spring ya apunta a esta BD, es una buena práctica incluir el USE.

USE `premier_league_db`;

-- NOTA IMPORTANTE: 
-- Las sentencias CREATE TABLE para 'usuarios', 'comentarios', 'favoritos_equipo', etc., 
-- NO son necesarias aquí, ya que serán generadas y ejecutadas 
-- AUTOMÁTICAMENTE por Spring Data JPA y Hibernate 
-- debido a la propiedad 'spring.jpa.hibernate.ddl-auto=update'. 
-- Hibernate utiliza las anotaciones @Entity y @Table de tus clases Java
-- (como Usuario.java) para crear el esquema de las tablas.

-- Si deseas precargar datos iniciales (ej: un usuario administrador), 
-- puedes añadirlos aquí, pero generalmente se usa 'data.sql' para eso.