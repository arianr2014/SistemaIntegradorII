-- ============================================================
-- BASE DE DATOS: bd_famacia
-- SISTEMA: FARMACIA
-- MOTOR: MySQL
-- ============================================================

DROP DATABASE IF EXISTS bd_famacia;

CREATE DATABASE bd_famacia
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE bd_famacia;


-- ============================================================
-- 1. CATEGORIA DE MEDICAMENTOS
-- ============================================================

CREATE TABLE categoria_medicamento (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
);


-- ============================================================
-- 2. MEDICAMENTOS
-- ============================================================

CREATE TABLE medicamento (
    id_medicamento INT AUTO_INCREMENT PRIMARY KEY,

    codigo VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    principio_activo VARCHAR(150),
    concentracion VARCHAR(100),
    forma_farmaceutica VARCHAR(100),
    presentacion VARCHAR(100),

    id_categoria INT,

    precio_venta DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    requiere_receta BOOLEAN DEFAULT TRUE,

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_medicamento_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categoria_medicamento(id_categoria)
);


-- ============================================================
-- 3. PROVEEDORES
-- ============================================================

CREATE TABLE proveedor (
    id_proveedor INT AUTO_INCREMENT PRIMARY KEY,

    razon_social VARCHAR(150) NOT NULL,
    ruc VARCHAR(20),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(255),

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
);


-- ============================================================
-- 4. LOTES DE MEDICAMENTOS
-- ============================================================

CREATE TABLE lote_medicamento (
    id_lote INT AUTO_INCREMENT PRIMARY KEY,

    id_medicamento INT NOT NULL,
    id_proveedor INT,

    numero_lote VARCHAR(50) NOT NULL,
    fecha_fabricacion DATE,
    fecha_vencimiento DATE NOT NULL,

    cantidad_inicial INT NOT NULL DEFAULT 0,
    cantidad_disponible INT NOT NULL DEFAULT 0,

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_lote_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES medicamento(id_medicamento),

    CONSTRAINT fk_lote_proveedor
        FOREIGN KEY (id_proveedor)
        REFERENCES proveedor(id_proveedor)
);


-- ============================================================
-- 5. INVENTARIO
-- ============================================================

CREATE TABLE inventario (
    id_inventario INT AUTO_INCREMENT PRIMARY KEY,

    id_medicamento INT NOT NULL,

    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 10,
    ubicacion VARCHAR(100),

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_inventario_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES medicamento(id_medicamento),

    CONSTRAINT uk_inventario_medicamento
        UNIQUE (id_medicamento)
);


-- ============================================================
-- 6. USUARIOS DE FARMACIA
-- ============================================================

CREATE TABLE usuario_farmacia (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,

    username VARCHAR(50) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    rol VARCHAR(50),
    password_hash VARCHAR(255) NOT NULL,

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
);



-- ============================================================
-- 9. PRESCRIPCIONES
-- ============================================================
-- Esta tabla está preparada para la futura integración
-- con el sistema NovaSalud mediante SOA.
--
-- Los campos *_externo corresponden a identificadores
-- provenientes del sistema externo (NovaSalud).
-- ============================================================

CREATE TABLE prescripcion (
    id_prescripcion INT AUTO_INCREMENT PRIMARY KEY,

    codigo_prescripcion VARCHAR(50) NOT NULL UNIQUE,

    id_paciente_externo INT,
    id_medico_externo INT,
    id_atencion_externa INT,

    fecha_prescripcion DATETIME NOT NULL,

    instrucciones_generales VARCHAR(700),

    estado VARCHAR(30) DEFAULT 'PENDIENTE',

    fecha_recepcion DATETIME DEFAULT CURRENT_TIMESTAMP,

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO'
);


-- ============================================================
-- 10. DETALLE DE PRESCRIPCION
-- ============================================================
-- Contiene los medicamentos indicados en una prescripción.
-- ============================================================

CREATE TABLE prescripcion_detalle (
    id_detalle_prescripcion INT AUTO_INCREMENT PRIMARY KEY,

    id_prescripcion INT NOT NULL,

    id_medicamento INT,

    nombre_medicamento VARCHAR(150) NOT NULL,

    cantidad INT NOT NULL,

    dosis VARCHAR(100),
    frecuencia VARCHAR(100),
    duracion VARCHAR(100),
    indicaciones VARCHAR(500),

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_prescripcion_detalle
        FOREIGN KEY (id_prescripcion)
        REFERENCES prescripcion(id_prescripcion),

    CONSTRAINT fk_prescripcion_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES medicamento(id_medicamento)
);




-- ============================================================
-- 7. VENTAS
-- ============================================================
CREATE TABLE venta (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,

    codigo_venta VARCHAR(30) NOT NULL UNIQUE,

    -- DATOS DEL CLIENTE
    dni VARCHAR(15),
    nombres VARCHAR(50),
    apellidos VARCHAR(100),

    -- RELACIÓN CON PRESCRIPCIÓN
    id_prescripcion INT,

    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,

    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    igv DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL DEFAULT 0.00,

    metodo_pago VARCHAR(30),

    id_usuario INT,

    -- AUDITORÍA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_venta_prescripcion
        FOREIGN KEY (id_prescripcion)
        REFERENCES prescripcion(id_prescripcion),

    CONSTRAINT fk_venta_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuario_farmacia(id_usuario)
);

-- ============================================================
-- 8. DETALLE DE VENTA
-- ============================================================

CREATE TABLE venta_detalle (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,

    id_venta INT NOT NULL,
    id_medicamento INT NOT NULL,

    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    -- AUDITORIA
    usuario_registro VARCHAR(50) DEFAULT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_actualizacion VARCHAR(50) DEFAULT NULL,
    fecha_actualizacion DATETIME DEFAULT NULL,
    estado_registro VARCHAR(15) NOT NULL DEFAULT 'ACTIVO',

    CONSTRAINT fk_venta_detalle_venta
        FOREIGN KEY (id_venta)
        REFERENCES venta(id_venta),

    CONSTRAINT fk_venta_detalle_medicamento
        FOREIGN KEY (id_medicamento)
        REFERENCES medicamento(id_medicamento)
);



-- ============================================================
-- ÍNDICES PARA MEJORAR CONSULTAS
-- ============================================================

CREATE INDEX idx_medicamento_nombre
    ON medicamento(nombre);

CREATE INDEX idx_medicamento_categoria
    ON medicamento(id_categoria);

CREATE INDEX idx_lote_medicamento
    ON lote_medicamento(id_medicamento);

CREATE INDEX idx_lote_vencimiento
    ON lote_medicamento(fecha_vencimiento);

CREATE INDEX idx_inventario_stock
    ON inventario(stock_actual);

CREATE INDEX idx_venta_fecha
    ON venta(fecha_venta);

CREATE INDEX idx_venta_detalle_venta
    ON venta_detalle(id_venta);

CREATE INDEX idx_prescripcion_paciente
    ON prescripcion(id_paciente_externo);

CREATE INDEX idx_prescripcion_atencion
    ON prescripcion(id_atencion_externa);

CREATE INDEX idx_prescripcion_estado
    ON prescripcion(estado);

CREATE INDEX idx_prescripcion_detalle_prescripcion
    ON prescripcion_detalle(id_prescripcion);


-- ============================================================
-- VERIFICACION DE TABLAS
-- ============================================================

SHOW TABLES;