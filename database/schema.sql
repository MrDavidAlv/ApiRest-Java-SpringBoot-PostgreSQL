-- ==========================================================
-- ENTERPRISE MANAGEMENT API - DATABASE SCHEMA
-- PostgreSQL 14+
-- ==========================================================

-- ==========================================================
-- 1. SECURITY AND CATALOGS
-- ==========================================================

CREATE TABLE IF NOT EXISTS roles (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL,
    descripcion TEXT
);

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    correo VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol_id INT REFERENCES roles(id),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_crea_id INT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS monedas (
    id SERIAL PRIMARY KEY,
    codigo_iso VARCHAR(3) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    simbolo VARCHAR(5) NOT NULL
);

-- ==========================================================
-- 2. BUSINESS MODULE (COMPANIES AND PRODUCTS)
-- ==========================================================

CREATE TABLE IF NOT EXISTS empresas (
    nit VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(20),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_crea_id INT REFERENCES usuarios(id),
    usuario_modifica_id INT REFERENCES usuarios(id),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS productos (
    codigo VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    caracteristicas TEXT,
    empresa_nit VARCHAR(20) REFERENCES empresas(nit),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    usuario_crea_id INT REFERENCES usuarios(id),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS producto_precios (
    id BIGSERIAL PRIMARY KEY,
    producto_codigo VARCHAR(50) REFERENCES productos(codigo) ON DELETE CASCADE,
    moneda_id INT REFERENCES monedas(id),
    precio DECIMAL(15,2) NOT NULL,
    CONSTRAINT uq_producto_moneda UNIQUE(producto_codigo, moneda_id)
);

CREATE TABLE IF NOT EXISTS categorias (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS producto_categoria (
    producto_codigo VARCHAR(50) REFERENCES productos(codigo) ON DELETE CASCADE,
    categoria_id INT REFERENCES categorias(id) ON DELETE CASCADE,
    PRIMARY KEY (producto_codigo, categoria_id)
);

-- ==========================================================
-- 3. TRANSACTIONAL MODULE (ORDERS)
-- ==========================================================

CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    documento VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS estados_orden (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS ordenes (
    id BIGSERIAL PRIMARY KEY,
    cliente_id INT REFERENCES clientes(id),
    estado_id INT REFERENCES estados_orden(id),
    fecha_orden TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_crea_id INT REFERENCES usuarios(id)
);

CREATE TABLE IF NOT EXISTS orden_detalles (
    id BIGSERIAL PRIMARY KEY,
    orden_id BIGINT REFERENCES ordenes(id) ON DELETE CASCADE,
    producto_codigo VARCHAR(50) REFERENCES productos(codigo),
    cantidad INT NOT NULL,
    precio_unitario_historico DECIMAL(15,2) NOT NULL
);

-- ==========================================================
-- 4. MONITORING AND ERROR LOGGING
-- ==========================================================

CREATE TABLE IF NOT EXISTS excepciones_sistema (
    id BIGSERIAL PRIMARY KEY,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id INT REFERENCES usuarios(id),
    clase_error VARCHAR(255),
    mensaje_error TEXT,
    stack_trace TEXT,
    endpoint_url VARCHAR(255),
    metodo_http VARCHAR(10),
    parametros_entrada JSONB,
    ip_origen VARCHAR(45),
    estado_resolucion VARCHAR(20) DEFAULT 'PENDIENTE'
);

-- ==========================================================
-- 5. INDEXES FOR PERFORMANCE
-- ==========================================================

CREATE INDEX IF NOT EXISTS idx_prod_empresa ON productos(empresa_nit);
CREATE INDEX IF NOT EXISTS idx_error_fecha ON excepciones_sistema(fecha_hora DESC);
CREATE INDEX IF NOT EXISTS idx_orden_cliente ON ordenes(cliente_id);
CREATE INDEX IF NOT EXISTS idx_orden_estado ON ordenes(estado_id);
CREATE INDEX IF NOT EXISTS idx_usuario_correo ON usuarios(correo);

-- ==========================================================
-- 6. INITIAL DATA
-- ==========================================================

-- Roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrator with full access'),
('EXTERNO', 'External user with read-only access')
ON CONFLICT (nombre) DO NOTHING;

-- Monedas
INSERT INTO monedas (codigo_iso, nombre, simbolo) VALUES
('COP', 'Peso Colombiano', '$'),
('USD', 'Dólar Estadounidense', '$'),
('EUR', 'Euro', '€')
ON CONFLICT (codigo_iso) DO NOTHING;

-- Estados de orden
INSERT INTO estados_orden (nombre) VALUES
('PENDIENTE'),
('PAGADA'),
('ANULADA')
ON CONFLICT (nombre) DO NOTHING;

-- Usuario administrador
-- Password: Admin123! (BCrypt cost 12)
INSERT INTO usuarios (correo, password, rol_id, activo) VALUES
('admin@litethinking.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYKKMQzzzm6',
    (SELECT id FROM roles WHERE nombre = 'ADMIN'), true)
ON CONFLICT (correo) DO NOTHING;

-- Usuario externo
-- Password: Externo123! (BCrypt cost 12)
INSERT INTO usuarios (correo, password, rol_id, activo) VALUES
('externo@litethinking.com', '$2a$12$8RrKQJZGh.VYXxUj8rDcO.HfY8hxRxq4pS7MhY8yZqN5LXqJq7RXG',
    (SELECT id FROM roles WHERE nombre = 'EXTERNO'), true)
ON CONFLICT (correo) DO NOTHING;

-- Categorías de ejemplo
INSERT INTO categorias (nombre, activo) VALUES
('Electrónica', true),
('Alimentos', true),
('Ropa', true),
('Hogar', true)
ON CONFLICT DO NOTHING;

-- Cliente de prueba
INSERT INTO clientes (documento, nombre, correo) VALUES
('1234567890', 'Cliente de Prueba', 'cliente@test.com')
ON CONFLICT (documento) DO NOTHING;

COMMIT;
