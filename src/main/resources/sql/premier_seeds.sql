-- src/main/resources/sql/premier_seeds.sql
-- Script de Inserción de Datos Iniciales (Seeds)

-- 1. Inserción de Roles
INSERT INTO `roles` (`id_rol`, `nombre`) VALUES
(1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

-- 2. Inserción de Permisos
INSERT INTO `permisos` (`nombre`) VALUES
('VIEW_ADMIN_DASHBOARD'),
('DELETE_COMMENT'),
('BLOCK_USER');

-- 3. Asignación de Permisos al Rol ADMIN (id_rol=2)
-- El rol USER (id_rol=1) no tiene permisos especiales por defecto
INSERT INTO `rol_permiso` (`id_rol`, `id_permiso`)
SELECT 2, p.id_permiso FROM `permisos` p WHERE p.nombre IN 
('VIEW_ADMIN_DASHBOARD', 'DELETE_COMMENT', 'BLOCK_USER');

-- 4. Inserción de un Usuario Administrador Inicial
-- La contraseña 'password' ha sido encriptada con BCrypt
INSERT INTO `usuarios` (`id_rol`, `nombre_usuario`, `email`, `password_hash`) VALUES
(2, 'admin_premier', 'admin@premier.com', '$2a$10$S4k/W5A2.VwM7R5qP8bK3O.D0Q9qN3H7O6P5l4R3M2N1X0Y9Z8A7B6C5D4E3F2G1H');

-- 5. Inserción de un Usuario Estándar (id_rol=1)
-- La contraseña 'password' ha sido encriptada con BCrypt
INSERT INTO `usuarios` (`id_rol`, `nombre_usuario`, `email`, `password_hash`) VALUES
(1, 'holahola', 'user@premier.com', '$2a$10$S4k/W5A2.VwM7R5qP8bK3O.D0Q9qN3H7O6P5l4R3M2N1X0Y9Z8A7B6C5D4E3F2G1H');