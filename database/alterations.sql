-- =========================================================
-- ALTERACIONES BASE DE DATOS - Sistema Enterprise
-- Ejecutar en orden en tu base de datos RDS PostgreSQL
-- =========================================================

-- 1. AGREGAR COLUMNA url_imagen A productos
ALTER TABLE productos ADD COLUMN IF NOT EXISTS url_imagen VARCHAR(500);

-- 2. AGREGAR COLUMNAS nombre_mostrar y avatar A usuarios
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS nombre_mostrar VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS avatar VARCHAR(50) DEFAULT 'avatar1.webp';

-- 3. CREAR TABLA uploads (para gestionar archivos subidos)
CREATE TABLE IF NOT EXISTS uploads (
    id BIGSERIAL PRIMARY KEY,
    nombre_archivo VARCHAR(255) NOT NULL,
    nombre_original VARCHAR(255) NOT NULL,
    tipo_contenido VARCHAR(100) NOT NULL,
    tamanio BIGINT NOT NULL,
    ruta VARCHAR(500) NOT NULL,
    fecha_subida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT REFERENCES usuarios(id)
);

CREATE INDEX IF NOT EXISTS idx_upload_usuario ON uploads(usuario_id);
CREATE INDEX IF NOT EXISTS idx_upload_fecha ON uploads(fecha_subida DESC);

-- 4. CREAR TABLA password_reset_tokens (para recuperación de contraseña)
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    token VARCHAR(255) UNIQUE NOT NULL,
    fecha_expiracion TIMESTAMP NOT NULL,
    usado BOOLEAN DEFAULT FALSE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_token_usuario ON password_reset_tokens(usuario_id);
CREATE INDEX IF NOT EXISTS idx_token_value ON password_reset_tokens(token);
CREATE INDEX IF NOT EXISTS idx_token_expiracion ON password_reset_tokens(fecha_expiracion);

-- 5. ACTUALIZAR USUARIOS EXISTENTES con valores por defecto
UPDATE usuarios SET avatar = 'avatar1.webp' WHERE avatar IS NULL;

COMMIT;

-- =========================================================
-- VERIFICACIÓN (ejecutar después de las alteraciones)
-- =========================================================

-- Verificar nuevas columnas en productos
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'productos' AND column_name IN ('url_imagen');

-- Verificar nuevas columnas en usuarios
SELECT column_name, data_type, character_maximum_length 
FROM information_schema.columns 
WHERE table_name = 'usuarios' AND column_name IN ('nombre_mostrar', 'avatar');

-- Verificar tabla uploads
SELECT COUNT(*) as tabla_uploads_existe FROM information_schema.tables WHERE table_name = 'uploads';

-- Verificar tabla password_reset_tokens
SELECT COUNT(*) as tabla_tokens_existe FROM information_schema.tables WHERE table_name = 'password_reset_tokens';
