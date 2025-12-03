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
-- La contraseña 'password' ha sido encriptada con BCrypt (generado con Spring Security)
INSERT INTO `usuarios` (`id_rol`, `nombre_usuario`, `email`, `password_hash`) VALUES
(2, 'admin_premier', 'admin@premier.com', '$2a$12$nGs.OKzuFwjaXL4LznU/SOzIT/dWarvaeKr2qqzpt8ZatBIHAFM7i');

-- 5. Inserción de un Usuario Estándar (id_rol=1)
-- La contraseña 'password' ha sido encriptada con BCrypt (generado con Spring Security)
INSERT INTO `usuarios` (`id_rol`, `nombre_usuario`, `email`, `password_hash`) VALUES
(1, 'holahola', 'user@premier.com', '$2a$12$nGs.OKzuFwjaXL4LznU/SOzIT/dWarvaeKr2qqzpt8ZatBIHAFM7i');