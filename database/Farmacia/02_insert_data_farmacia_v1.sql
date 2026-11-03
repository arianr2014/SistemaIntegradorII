-- =============================================================================
-- SCRIPT DE INSERCIÓN DE DATOS INICIALES (POBLAMIENTO) DE FARMACIA
-- Base de Datos: bd_famacia
-- Motor: MySQL 8.0
-- =============================================================================

USE bd_famacia;

-- Desactivar temporalmente revisión de claves foráneas para limpieza segura
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE venta_detalle;
TRUNCATE TABLE venta;
TRUNCATE TABLE prescripcion_detalle;
TRUNCATE TABLE prescripcion;
TRUNCATE TABLE inventario;
TRUNCATE TABLE lote_medicamento;
TRUNCATE TABLE medicamento;
TRUNCATE TABLE proveedor;
TRUNCATE TABLE categoria_medicamento;
TRUNCATE TABLE usuario_farmacia;

SET FOREIGN_KEY_CHECKS = 1;


-- =============================================================================
-- 1. CATEGORIAS DE MEDICAMENTOS
-- =============================================================================
INSERT INTO categoria_medicamento (id_categoria, nombre, descripcion, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 'Analgésicos y Antipiréticos', 'Medicamentos para el alivio del dolor de leve a moderado y reducción de la fiebre', 'admin', NOW(), 'ACTIVO'),
(2, 'Antiinflamatorios y Relajantes Musculares', 'Medicamentos para reducir inflamaciones musculares, articulares y contracturas', 'admin', NOW(), 'ACTIVO'),
(3, 'Antigripales y Descongestionantes', 'Medicamentos multisíntoma para el tratamiento de la gripe, resfrío y congestión nasal', 'admin', NOW(), 'ACTIVO'),
(4, 'Antibióticos y Antiinfecciosos', 'Agentes antimicrobianos para combatir infecciones bacterianas respiratorias y secundarias', 'admin', NOW(), 'ACTIVO'),
(5, 'Antihistamínicos y Antialérgicos', 'Medicamentos para el control de alergias respiratorias, rinitis y síntomas estornutatorios', 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 2. PROVEEDORES
-- =============================================================================
INSERT INTO proveedor (id_proveedor, razon_social, ruc, telefono, correo, direccion, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 'Medifarma S.A.', '20100034561', '01-7104000', 'contacto@medifarma.com.pe', 'Av. Alfredo Mendiola 5600, Los Olivos, Lima', 'admin', NOW(), 'ACTIVO'),
(2, 'Laboratorios Farmacéuticos Perú S.A.C.', '20509988771', '01-4456789', 'ventas@farmaperu.com.pe', 'Av. Las Camelias 820, San Isidro, Lima', 'admin', NOW(), 'ACTIVO'),
(3, 'Química Farmacéutica Bayer Perú S.A.', '20100098765', '01-2113800', 'servicio.cliente@bayer.com.pe', 'Av. Paseo de la República 3074, San Isidro, Lima', 'admin', NOW(), 'ACTIVO'),
(4, 'Laboratorios Portugal S.R.L.', '20100234129', '054-234567', 'ventas@labportugal.com', 'Calle Jacinto Ibáñez 315, Parque Industrial, Arequipa', 'admin', NOW(), 'ACTIVO'),
(5, 'Distribuidora Médica FarmaSalud E.I.R.L.', '20601234567', '01-3329090', 'distribucion@farmasalud.com.pe', 'Av. Brasil 1450, Pueblo Libre, Lima', 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 3. MEDICAMENTOS (20 Medicamentos para Fiebre, Gripe y Dolores Musculares)
-- =============================================================================
INSERT INTO medicamento (id_medicamento, codigo, nombre, principio_activo, concentracion, forma_farmaceutica, presentacion, id_categoria, precio_venta, requiere_receta, usuario_registro, fecha_registro, estado_registro) VALUES
-- Fiebre y Dolor (Analgésicos y Antipiréticos)
(1, 'MED-PAR-500', 'Paracetamol 500 mg Tabletas', 'Paracetamol', '500 mg', 'Tableta', 'Caja x 100 tabletas', 1, 0.50, FALSE, 'admin', NOW(), 'ACTIVO'),
(2, 'MED-PAR-1000', 'Paracetamol 1 g Tabletas Forte', 'Paracetamol', '1000 mg', 'Tableta recubierta', 'Caja x 20 tabletas', 1, 1.20, FALSE, 'admin', NOW(), 'ACTIVO'),
(3, 'MED-MET-500', 'Antalgina 500 mg (Metamizol)', 'Metamizol Sódico', '500 mg', 'Tableta', 'Caja x 100 tabletas', 1, 0.80, FALSE, 'admin', NOW(), 'ACTIVO'),
(4, 'MED-PAN-FOR', 'Panadol Forte Tabletas', 'Paracetamol + Cafeína', '500 mg / 65 mg', 'Tableta', 'Caja x 24 tabletas', 1, 1.50, FALSE, 'admin', NOW(), 'ACTIVO'),

-- Dolores Musculares e Inflamación (Antiinflamatorios y Relajantes Musculares)
(5, 'MED-IBU-400', 'Ibuprofeno 400 mg Tabletas', 'Ibuprofeno', '400 mg', 'Tableta recubierta', 'Caja x 100 tabletas', 2, 0.70, FALSE, 'admin', NOW(), 'ACTIVO'),
(6, 'MED-IBU-600', 'Ibuprofeno 600 mg Tabletas', 'Ibuprofeno', '600 mg', 'Tableta recubierta', 'Caja x 50 tabletas', 2, 1.10, FALSE, 'admin', NOW(), 'ACTIVO'),
(7, 'MED-NAP-550', 'Naproxeno Sódico 550 mg (Apronax)', 'Naproxeno Sódico', '550 mg', 'Tableta recubierta', 'Caja x 20 tabletas', 2, 2.50, FALSE, 'admin', NOW(), 'ACTIVO'),
(8, 'MED-KET-10', 'Ketorolaco 10 mg Tabletas Sublinguales', 'Ketorolaco Trometamol', '10 mg', 'Tableta sublingual', 'Caja x 10 tabletas', 2, 1.80, TRUE, 'admin', NOW(), 'ACTIVO'),
(9, 'MED-DIC-50', 'Diclofenaco Sódico 50 mg', 'Diclofenaco Sódico', '50 mg', 'Tableta entérica', 'Caja x 100 tabletas', 2, 0.90, FALSE, 'admin', NOW(), 'ACTIVO'),
(10, 'MED-DIC-75', 'Diclofenaco 75 mg / 3 ml Ampollas', 'Diclofenaco Sódico', '75 mg / 3 ml', 'Solución Inyectable', 'Caja x 5 ampollas', 2, 4.50, TRUE, 'admin', NOW(), 'ACTIVO'),
(11, 'MED-ORF-100', 'Orfenadrina Citrato 100 mg', 'Orfenadrina Citrato', '100 mg', 'Tableta de liberación prolongada', 'Caja x 30 tabletas', 2, 2.00, TRUE, 'admin', NOW(), 'ACTIVO'),
(12, 'MED-DOL-MUS', 'Dolaren Tabletas (Paracetamol + Orfenadrina)', 'Paracetamol + Orfenadrina', '450 mg / 35 mg', 'Tableta', 'Caja x 100 tabletas', 2, 1.60, FALSE, 'admin', NOW(), 'ACTIVO'),
(13, 'MED-CEL-200', 'Celecoxib 200 mg Cápsulas', 'Celecoxib', '200 mg', 'Cápsula dura', 'Caja x 20 cápsulas', 2, 3.20, TRUE, 'admin', NOW(), 'ACTIVO'),

-- Gripes, Resfríos y Congestión Nasal (Antigripales y Descongestionantes)
(14, 'MED-NAS-COM', 'Nastizol Compuesto Tabletas', 'Paracetamol + Pseudoefedrina + Clorfenamina', '500 mg / 60 mg / 4 mg', 'Tableta recubierta', 'Caja x 20 tabletas', 3, 2.20, FALSE, 'admin', NOW(), 'ACTIVO'),
(15, 'MED-DRI-ANT', 'Dristan Antigripal Tabletas', 'Paracetamol + Fenilefrina + Clorfenamina', '500 mg / 5 mg / 2 mg', 'Tableta', 'Caja x 24 tabletas', 3, 1.80, FALSE, 'admin', NOW(), 'ACTIVO'),
(16, 'MED-REF-ANT', 'Refrianex Antigripal Granulado', 'Paracetamol + Pseudoefedrina + Clorfenamina', '500 mg / 60 mg / 4 mg', 'Sobres Granulados', 'Caja x 12 sobres', 3, 3.00, FALSE, 'admin', NOW(), 'ACTIVO'),

-- Antibióticos Respiratorios (Antibióticos)
(17, 'MED-AMO-500', 'Amoxicilina 500 mg Cápsulas', 'Amoxicilina Trihidrato', '500 mg', 'Cápsula', 'Caja x 100 cápsulas', 4, 1.30, TRUE, 'admin', NOW(), 'ACTIVO'),
(18, 'MED-AZI-500', 'Azitromicina 500 mg Tabletas', 'Azitromicina Dihidrato', '500 mg', 'Tableta recubierta', 'Caja x 3 tabletas', 4, 5.00, TRUE, 'admin', NOW(), 'ACTIVO'),

-- Síntomas Alérgicos de Gripe (Antihistamínicos)
(19, 'MED-LOR-10', 'Loratadina 10 mg Tabletas', 'Loratadina', '10 mg', 'Tableta', 'Caja x 100 tabletas', 5, 0.60, FALSE, 'admin', NOW(), 'ACTIVO'),
(20, 'MED-CET-10', 'Cetirizina 10 mg Tabletas Recubiertas', 'Cetirizina Diclorhidrato', '10 mg', 'Tableta recubierta', 'Caja x 30 tabletas', 5, 0.90, FALSE, 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 4. LOTES DE MEDICAMENTOS (Stock con Vencimientos 2027/2028)
-- =============================================================================
INSERT INTO lote_medicamento (id_lote, id_medicamento, id_proveedor, numero_lote, fecha_fabricacion, fecha_vencimiento, cantidad_inicial, cantidad_disponible, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 1, 1, 'LOT-PAR-202601', '2026-01-10', '2028-01-10', 1000, 850, 'admin', NOW(), 'ACTIVO'),
(2, 2, 1, 'LOT-PAR-202602', '2026-02-01', '2028-02-01', 500, 420, 'admin', NOW(), 'ACTIVO'),
(3, 3, 4, 'LOT-MET-202601', '2026-01-15', '2027-12-31', 800, 700, 'admin', NOW(), 'ACTIVO'),
(4, 4, 2, 'LOT-PAN-202603', '2026-03-01', '2028-03-01', 600, 510, 'admin', NOW(), 'ACTIVO'),
(5, 5, 1, 'LOT-IBU-202601', '2026-01-20', '2028-01-20', 1200, 1100, 'admin', NOW(), 'ACTIVO'),
(6, 6, 1, 'LOT-IBU-202602', '2026-02-10', '2028-02-10', 900, 780, 'admin', NOW(), 'ACTIVO'),
(7, 7, 3, 'LOT-NAP-202601', '2026-01-05', '2028-01-05', 400, 350, 'admin', NOW(), 'ACTIVO'),
(8, 8, 2, 'LOT-KET-202601', '2026-02-15', '2027-11-30', 300, 260, 'admin', NOW(), 'ACTIVO'),
(9, 9, 4, 'LOT-DIC-202601', '2026-01-18', '2028-01-18', 750, 640, 'admin', NOW(), 'ACTIVO'),
(10, 10, 4, 'LOT-DIC-202602', '2026-02-05', '2027-10-15', 200, 180, 'admin', NOW(), 'ACTIVO'),
(11, 11, 2, 'LOT-ORF-202601', '2026-01-12', '2028-01-12', 500, 430, 'admin', NOW(), 'ACTIVO'),
(12, 12, 1, 'LOT-DOL-202601', '2026-02-20', '2028-02-20', 800, 710, 'admin', NOW(), 'ACTIVO'),
(13, 13, 3, 'LOT-CEL-202601', '2026-01-25', '2027-12-15', 350, 310, 'admin', NOW(), 'ACTIVO'),
(14, 14, 2, 'LOT-NAS-202601', '2026-02-01', '2028-02-01', 650, 560, 'admin', NOW(), 'ACTIVO'),
(15, 15, 5, 'LOT-DRI-202601', '2026-01-30', '2028-01-30', 550, 490, 'admin', NOW(), 'ACTIVO'),
(16, 16, 5, 'LOT-REF-202601', '2026-02-12', '2027-09-30', 400, 340, 'admin', NOW(), 'ACTIVO'),
(17, 17, 1, 'LOT-AMO-202601', '2026-01-14', '2027-08-31', 1000, 890, 'admin', NOW(), 'ACTIVO'),
(18, 18, 2, 'LOT-AZI-202601', '2026-02-18', '2027-07-31', 450, 390, 'admin', NOW(), 'ACTIVO'),
(19, 19, 4, 'LOT-LOR-202601', '2026-01-22', '2028-01-22', 900, 820, 'admin', NOW(), 'ACTIVO'),
(20, 20, 5, 'LOT-CET-202601', '2026-02-22', '2028-02-22', 700, 620, 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 5. INVENTARIO (Control de Stock Actual y Ubicación por Estante)
-- =============================================================================
INSERT INTO inventario (id_inventario, id_medicamento, stock_actual, stock_minimo, ubicacion, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 1, 850, 100, 'Estante A-1 (Analgésicos)', 'admin', NOW(), 'ACTIVO'),
(2, 2, 420, 50, 'Estante A-1 (Analgésicos)', 'admin', NOW(), 'ACTIVO'),
(3, 3, 700, 80, 'Estante A-2 (Antipiréticos)', 'admin', NOW(), 'ACTIVO'),
(4, 4, 510, 60, 'Estante A-2 (Antipiréticos)', 'admin', NOW(), 'ACTIVO'),
(5, 5, 1100, 150, 'Estante B-1 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(6, 6, 780, 100, 'Estante B-1 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(7, 7, 350, 40, 'Estante B-2 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(8, 8, 260, 30, 'Estante B-2 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(9, 9, 640, 80, 'Estante B-3 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(10, 10, 180, 20, 'Estante Refrigerado / Inyectables I-1', 'admin', NOW(), 'ACTIVO'),
(11, 11, 430, 50, 'Estante C-1 (Relajantes Musculares)', 'admin', NOW(), 'ACTIVO'),
(12, 12, 710, 90, 'Estante C-1 (Relajantes Musculares)', 'admin', NOW(), 'ACTIVO'),
(13, 13, 310, 40, 'Estante C-2 (Antiinflamatorios)', 'admin', NOW(), 'ACTIVO'),
(14, 14, 560, 70, 'Estante D-1 (Antigripales)', 'admin', NOW(), 'ACTIVO'),
(15, 15, 490, 60, 'Estante D-1 (Antigripales)', 'admin', NOW(), 'ACTIVO'),
(16, 16, 340, 50, 'Estante D-2 (Antigripales)', 'admin', NOW(), 'ACTIVO'),
(17, 17, 890, 100, 'Estante E-1 (Antibióticos)', 'admin', NOW(), 'ACTIVO'),
(18, 18, 390, 50, 'Estante E-1 (Antibióticos)', 'admin', NOW(), 'ACTIVO'),
(19, 19, 820, 100, 'Estante F-1 (Antihistamínicos)', 'admin', NOW(), 'ACTIVO'),
(20, 20, 620, 80, 'Estante F-1 (Antihistamínicos)', 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 6. USUARIOS DE FARMACIA (Contraseñas encriptadas con BCrypt: Farmacia2026*)
-- =============================================================================
-- Contraseña plana por defecto para todos los usuarios: Farmacia2026*
INSERT INTO usuario_farmacia (id_usuario, username, nombres, apellidos, rol, password_hash, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 'admin', 'Carlos Eduardo', 'Mendoza Ramírez', 'ADMINISTRADOR', '$2a$12$ACgLkSwZBvZztr9EI3sZsOggQdTNAxBmRDjVdtoXk7KxCZoZymz9G', 'sistema', NOW(), 'ACTIVO'),
(2, 'qf_martinez', 'María Elena', 'Martínez Paredes', 'QUIMICO_FARMACEUTICO', '$2a$12$ACgLkSwZBvZztr9EI3sZsOggQdTNAxBmRDjVdtoXk7KxCZoZymz9G', 'admin', NOW(), 'ACTIVO'),
(3, 'cajero01', 'Juan José', 'Sánchez Torres', 'TECNICO_FARMACIA', '$2a$12$ACgLkSwZBvZztr9EI3sZsOggQdTNAxBmRDjVdtoXk7KxCZoZymz9G', 'admin', NOW(), 'ACTIVO');


-- =============================================================================
-- 7. PRESCRIPCIONES (Prueba de Integración SOA con NovaSalud Citas/Atención)
-- =============================================================================
INSERT INTO prescripcion (id_prescripcion, codigo_prescripcion, id_paciente_externo, id_medico_externo, id_atencion_externa, fecha_prescripcion, instrucciones_generales, estado, fecha_recepcion, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 'PRES-2026-0001', 1, 2, 101, '2026-09-04 10:30:00', 'Tomar los medicamentos indicados junto con alimentos. Reposo por 48 horas e hidratación abundante.', 'ATENDIDA', '2026-09-04 11:00:00', 'qf_martinez', NOW(), 'ACTIVO'),
(2, 'PRES-2026-0002', 3, 5, 102, '2026-09-05 09:15:00', 'Tratamiento para sindrome gripal y contractura muscular cervical. Cumplir dosis exacta.', 'PENDIENTE', '2026-09-05 09:30:00', 'qf_martinez', NOW(), 'ACTIVO');


-- =============================================================================
-- 8. DETALLE DE PRESCRIPCION
-- =============================================================================
INSERT INTO prescripcion_detalle (id_detalle_prescripcion, id_prescripcion, id_medicamento, nombre_medicamento, cantidad, dosis, frecuencia, duracion, indicaciones, usuario_registro, fecha_registro, estado_registro) VALUES
-- Detalles Prescripción 1
(1, 1, 1, 'Paracetamol 500 mg Tabletas', 20, '1 tableta (500mg)', 'Cada 8 horas', '5 días', 'Tomar con medio vaso de agua en caso de fiebre superior a 38°C', 'qf_martinez', NOW(), 'ACTIVO'),
(2, 1, 14, 'Nastizol Compuesto Tabletas', 10, '1 tableta', 'Cada 12 horas', '5 días', 'Para la congestión nasal y estornudos', 'qf_martinez', NOW(), 'ACTIVO'),

-- Detalles Prescripción 2
(3, 2, 7, 'Naproxeno Sódico 550 mg (Apronax)', 10, '1 tableta (550mg)', 'Cada 12 horas', '5 días', 'Tomar inmediatamente después del almuerzo y cena', 'qf_martinez', NOW(), 'ACTIVO'),
(4, 2, 12, 'Dolaren Tabletas (Paracetamol + Orfenadrina)', 15, '1 tableta', 'Cada 8 horas', '5 días', 'Para alivio de espasmo muscular cervical', 'qf_martinez', NOW(), 'ACTIVO');


-- =============================================================================
-- 9. VENTAS DE PRUEBA
-- =============================================================================
INSERT INTO venta (id_venta, codigo_venta, dni, nombres, apellidos, id_prescripcion, fecha_venta, subtotal, igv, total, metodo_pago, id_usuario, usuario_registro, fecha_registro, estado_registro) VALUES
(1, 'VEN-2026-0001', '45892301', 'Ana Lucia', 'Gómez Rivas', 1, '2026-09-04 11:15:00', '27.12', '4.88', '32.00', 'EFECTIVO', 3, 'cajero01', NOW(), 'ACTIVO'),
(2, 'VEN-2026-0002', '71239084', 'Roberto Carlos', 'Vásquez Silva', NULL, '2026-09-05 14:20:00', '16.95', '3.05', '20.00', 'TARJETA_DEBITO', 3, 'cajero01', NOW(), 'ACTIVO');


-- =============================================================================
-- 10. DETALLE DE VENTA
-- =============================================================================
INSERT INTO venta_detalle (id_detalle, id_venta, id_medicamento, cantidad, precio_unitario, subtotal, usuario_registro, fecha_registro, estado_registro) VALUES
-- Detalles Venta 1 (Asociada a Prescripción 1)
(1, 1, 1, 20, 0.50, 10.00, 'cajero01', NOW(), 'ACTIVO'),
(2, 1, 14, 10, 2.20, 22.00, 'cajero01', NOW(), 'ACTIVO'),

-- Detalles Venta 2 (Venta directa libre)
(3, 2, 4, 10, 1.50, 15.00, 'cajero01', NOW(), 'ACTIVO'),
(4, 2, 5, 5, 1.00, 5.00, 'cajero01', NOW(), 'ACTIVO');


-- =============================================================================
-- VERIFICACIÓN DE INSERCIÓN Y CONTEO DE FILAS
-- =============================================================================
SELECT 'categoria_medicamento' AS tabla, COUNT(*) AS total FROM categoria_medicamento
UNION ALL SELECT 'proveedor', COUNT(*) FROM proveedor
UNION ALL SELECT 'medicamento', COUNT(*) FROM medicamento
UNION ALL SELECT 'lote_medicamento', COUNT(*) FROM lote_medicamento
UNION ALL SELECT 'inventario', COUNT(*) FROM inventario
UNION ALL SELECT 'usuario_farmacia', COUNT(*) FROM usuario_farmacia
UNION ALL SELECT 'prescripcion', COUNT(*) FROM prescripcion
UNION ALL SELECT 'prescripcion_detalle', COUNT(*) FROM prescripcion_detalle
UNION ALL SELECT 'venta', COUNT(*) FROM venta
UNION ALL SELECT 'venta_detalle', COUNT(*) FROM venta_detalle;
