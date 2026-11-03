-- ============================================================
-- USUARIO MYSQL: usuario_farmacia
-- BASE DE DATOS: bd_famacia
-- ============================================================

-- 1. Crear usuario
CREATE USER IF NOT EXISTS 'usuario_farmacia'@'localhost'
IDENTIFIED BY 'Farmacia2026*';

-- 2. Dar todos los permisos sobre la base de datos
GRANT ALL PRIVILEGES
ON bd_famacia.*
TO 'usuario_farmacia'@'localhost';

-- 3. Aplicar permisos
FLUSH PRIVILEGES;