-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS bd_citasmedicas_v321;

-- Crear usuario
CREATE USER IF NOT EXISTS 'usuario_citas'@'localhost'
IDENTIFIED BY 'ISO/IEC27001';

-- Dar permisos sobre la base de datos
GRANT ALL PRIVILEGES ON bd_citasmedicas_v321.*
TO 'usuario_citas'@'localhost';

-- Aplicar cambios
FLUSH PRIVILEGES;