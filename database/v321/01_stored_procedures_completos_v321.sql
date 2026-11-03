-- =============================================================================
-- SCRIPT DE PROCEDIMIENTOS ALMACENADOS COMPLETOS DE NOVASALUD V3.2.1
-- Base de Datos: bd_citasmedicas_v321
-- =============================================================================

USE bd_citasmedicas_v321;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 1. PACIENTES
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_registrar_paciente$$
CREATE PROCEDURE sp_registrar_paciente(
    IN p_dni VARCHAR(20),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_direccion VARCHAR(255),
    IN p_usuario_registro VARCHAR(50)
)
BEGIN
    INSERT INTO paciente (dni, nombres, apellidos, telefono, direccion, usuario_registro, fecha_registro, estado_registro)
    VALUES (p_dni, p_nombres, p_apellidos, p_telefono, p_direccion, COALESCE(p_usuario_registro, 'sistema'), NOW(), 'ACTIVO');

    SELECT LAST_INSERT_ID() AS id_paciente;
END$$

DROP PROCEDURE IF EXISTS sp_actualizar_paciente$$
CREATE PROCEDURE sp_actualizar_paciente(
    IN p_id_paciente INT,
    IN p_dni VARCHAR(20),
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_direccion VARCHAR(255)
)
BEGIN
    UPDATE paciente SET
        dni = p_dni,
        nombres = p_nombres,
        apellidos = p_apellidos,
        telefono = p_telefono,
        direccion = p_direccion,
        fecha_actualizacion = NOW()
    WHERE id_paciente = p_id_paciente;
END$$

DROP PROCEDURE IF EXISTS sp_eliminar_paciente$$
CREATE PROCEDURE sp_eliminar_paciente(
    IN p_id INT
)
BEGIN
    UPDATE paciente SET estado_registro = 'INACTIVO', fecha_actualizacion = NOW() WHERE id_paciente = p_id;
END$$

-- -----------------------------------------------------------------------------
-- 2. MEDICOS
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_registrar_medico$$
CREATE PROCEDURE sp_registrar_medico(
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_id_especialidad INT,
    IN p_telefono VARCHAR(20),
    IN p_usuario_registro VARCHAR(50)
)
BEGIN
    INSERT INTO medico (nombres, apellidos, id_especialidad, telefono, usuario_registro, fecha_registro, estado_registro)
    VALUES (p_nombres, p_apellidos, p_id_especialidad, p_telefono, COALESCE(p_usuario_registro, 'sistema'), NOW(), 'ACTIVO');

    SELECT LAST_INSERT_ID() AS id_medico;
END$$

DROP PROCEDURE IF EXISTS sp_actualizar_medico$$
CREATE PROCEDURE sp_actualizar_medico(
    IN p_id_medico INT,
    IN p_nombres VARCHAR(100),
    IN p_apellidos VARCHAR(100),
    IN p_id_especialidad INT,
    IN p_telefono VARCHAR(20)
)
BEGIN
    UPDATE medico SET
        nombres = p_nombres,
        apellidos = p_apellidos,
        id_especialidad = p_id_especialidad,
        telefono = p_telefono,
        fecha_actualizacion = NOW()
    WHERE id_medico = p_id_medico;
END$$

DROP PROCEDURE IF EXISTS sp_eliminar_medico$$
CREATE PROCEDURE sp_eliminar_medico(
    IN p_id INT
)
BEGIN
    UPDATE medico SET estado_registro = 'INACTIVO', fecha_actualizacion = NOW() WHERE id_medico = p_id;
END$$

DROP PROCEDURE IF EXISTS sp_listar_medicos$$
CREATE PROCEDURE sp_listar_medicos()
BEGIN
    SELECT m.id_medico, CONCAT(m.nombres, ' ', m.apellidos) AS medico, e.nombre AS especialidad, m.telefono
    FROM medico m
    INNER JOIN especialidad e ON m.id_especialidad = e.id_especialidad
    WHERE m.estado_registro = 'ACTIVO' AND e.estado_registro = 'ACTIVO'
    ORDER BY e.nombre, m.apellidos;
END$$

-- -----------------------------------------------------------------------------
-- 3. ESPECIALIDADES
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_eliminar_especialidad$$
CREATE PROCEDURE sp_eliminar_especialidad(
    IN p_id INT
)
BEGIN
    UPDATE especialidad SET estado_registro = 'INACTIVO', fecha_actualizacion = NOW() WHERE id_especialidad = p_id;
END$$

-- -----------------------------------------------------------------------------
-- 4. HORARIOS
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_eliminar_horario$$
CREATE PROCEDURE sp_eliminar_horario(
    IN p_id INT
)
BEGIN
    UPDATE horario SET estado_registro = 'INACTIVO', fecha_actualizacion = NOW() WHERE id_horario = p_id;
END$$

-- -----------------------------------------------------------------------------
-- 5. CITAS MEDICAS
-- -----------------------------------------------------------------------------
DROP PROCEDURE IF EXISTS sp_registrar_cita$$
CREATE PROCEDURE sp_registrar_cita(
    IN p_id_paciente INT,
    IN p_id_medico INT,
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_estado VARCHAR(20),
    IN p_observaciones VARCHAR(255),
    IN p_usuario_registro VARCHAR(50)
)
BEGIN
    DECLARE v_id_especialidad INT DEFAULT 1;
    DECLARE v_id_tarifa INT DEFAULT 1;
    DECLARE v_monto DECIMAL(10,2) DEFAULT 60.00;
    DECLARE v_id_usuario INT DEFAULT NULL;

    SELECT id_especialidad INTO v_id_especialidad FROM medico WHERE id_medico = p_id_medico LIMIT 1;

    SELECT id_tarifa, monto INTO v_id_tarifa, v_monto
    FROM tarifa_consulta
    WHERE (id_especialidad = v_id_especialidad OR id_especialidad IS NULL)
      AND estado_registro = 'ACTIVO'
    ORDER BY id_especialidad DESC, id_tarifa ASC
    LIMIT 1;

    IF v_id_tarifa IS NULL THEN
        SET v_id_tarifa = 1;
        SET v_monto = 60.00;
    END IF;

    SELECT id_usuario INTO v_id_usuario FROM usuario WHERE username = p_usuario_registro LIMIT 1;

    INSERT INTO cita (
        id_paciente, id_medico, id_especialidad, id_tarifa_aplicada,
        id_usuario_recepcionista, monto_consulta, fecha, hora,
        duracion_minutos, estado, observaciones, usuario_registro,
        fecha_registro, estado_registro
    ) VALUES (
        p_id_paciente, p_id_medico, COALESCE(v_id_especialidad, 1), v_id_tarifa,
        v_id_usuario, v_monto, p_fecha, p_hora,
        30, COALESCE(NULLIF(TRIM(p_estado), ''), 'PROGRAMADA'), p_observaciones, COALESCE(p_usuario_registro, 'sistema'),
        NOW(), 'ACTIVO'
    );

    SELECT LAST_INSERT_ID() AS id_cita;
END$$

DROP PROCEDURE IF EXISTS sp_actualizar_cita$$
CREATE PROCEDURE sp_actualizar_cita(
    IN p_id_cita INT,
    IN p_id_paciente INT,
    IN p_id_medico INT,
    IN p_fecha DATE,
    IN p_hora TIME,
    IN p_estado VARCHAR(20),
    IN p_observaciones VARCHAR(255)
)
BEGIN
    DECLARE v_id_especialidad INT DEFAULT NULL;

    SELECT id_especialidad INTO v_id_especialidad FROM medico WHERE id_medico = p_id_medico LIMIT 1;

    UPDATE cita SET
        id_paciente = p_id_paciente,
        id_medico = p_id_medico,
        id_especialidad = COALESCE(v_id_especialidad, id_especialidad),
        fecha = p_fecha,
        hora = p_hora,
        estado = COALESCE(NULLIF(TRIM(p_estado), ''), estado),
        observaciones = p_observaciones,
        fecha_actualizacion = NOW()
    WHERE id_cita = p_id_cita;
END$$

DROP PROCEDURE IF EXISTS sp_eliminar_cita$$
CREATE PROCEDURE sp_eliminar_cita(
    IN p_id_cita INT
)
BEGIN
    UPDATE cita SET
        estado = 'ANULADA',
        estado_registro = 'INACTIVO',
        fecha_actualizacion = NOW()
    WHERE id_cita = p_id_cita;
END$$

DELIMITER ;
