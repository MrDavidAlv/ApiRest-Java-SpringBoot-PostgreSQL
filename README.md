# Enterprise Management API

API REST de nivel producción construida con Java 17, Spring Boot 3.2 y arquitectura hexagonal para la gestión empresarial, productos, clientes y órdenes.

## Arquitectura

Este proyecto implementa **Arquitectura Hexagonal (Ports & Adapters)** con separación clara de responsabilidades:

```
src/main/java/com/litethinking/enterprise/
├── domain/              # Lógica de negocio pura (sin dependencias externas)
│   ├── model/          # Entidades de dominio y Value Objects
│   ├── port/           # Interfaces (puertos de entrada y salida)
│   └── exception/      # Excepciones de dominio
├── application/         # Casos de uso y orquestación
│   ├── usecase/        # Servicios de aplicación
│   ├── dto/            # Data Transfer Objects
│   └── mapper/         # Mappers (MapStruct)
├── infrastructure/      # Implementaciones técnicas
│   ├── persistence/    # JPA entities y repositorios
│   ├── security/       # JWT, BCrypt, configuración de seguridad
│   ├── integration/    # reCAPTCHA, AWS SES, PDF
│   └── config/         # Configuraciones de Spring
└── interfaces/          # Adaptadores de entrada
    ├── rest/           # Controllers REST
    └── exception/      # Exception handlers globales
```

## Tecnologías

- **Java 17**
- **Spring Boot 3.2.1**
- **PostgreSQL** (base de datos principal)
- **Spring Security** + **JWT** (autenticación stateless)
- **BCrypt** (hashing de contraseñas, cost 12)
- **MapStruct** (mapeo de objetos)
- **Hibernate/JPA** (ORM)
- **Swagger/OpenAPI** (documentación)
- **Google reCAPTCHA v3** (protección de login)
- **AWS SES** (envío de correos)
- **iText7** (generación de PDF)
- **JUnit 5** + **Mockito** (testing)

## Requisitos Previos

- JDK 17 o superior
- Maven 3.8+
- PostgreSQL 14+
- Cuenta de Google Cloud (reCAPTCHA)
- Cuenta de AWS (SES para correos)

## Configuración de Base de Datos

Ejecutar el script SQL proporcionado para crear el esquema completo:

```sql
-- Ver archivo: database/schema.sql
```

El modelo incluye:
- Seguridad: `roles`, `usuarios`
- Catálogos: `monedas`, `categorias`
- Negocio: `empresas`, `productos`, `producto_precios`, `producto_categoria`
- Transaccional: `clientes`, `estados_orden`, `ordenes`, `orden_detalles`
- Monitoreo: `excepciones_sistema`

## Variables de Entorno

Crear archivo `.env` o configurar en el sistema:

```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=enterprise_db
DB_USER=postgres
DB_PASSWORD=your_password

# Security
JWT_SECRET=your-256-bit-secret-key-minimum-32-characters-long
RECAPTCHA_SECRET=your-recaptcha-secret-key

# AWS
AWS_REGION=us-east-1
AWS_ACCESS_KEY=your-access-key
AWS_SECRET_KEY=your-secret-key
AWS_SES_FROM=noreply@yourdomain.com
AWS_SES_ENABLED=true

# Server
SERVER_PORT=8080
```

## Ejecución Local

```bash
# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run

# O directamente
java -jar target/enterprise-api-1.0.0.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Documentación API

Swagger UI: `http://localhost:8080/swagger-ui.html`

OpenAPI JSON: `http://localhost:8080/api-docs`

**Nota:** Swagger requiere autenticación JWT. Primero hacer login y copiar el token en "Authorize".

## Endpoints Principales

### Autenticación

```
POST /api/v1/auth/login
POST /api/v1/auth/register
```

### Empresas (requiere ADMIN)

```
POST   /api/v1/empresas
GET    /api/v1/empresas
GET    /api/v1/empresas/{nit}
PUT    /api/v1/empresas/{nit}
DELETE /api/v1/empresas/{nit}
```

### Productos (requiere ADMIN)

```
POST   /api/v1/productos
GET    /api/v1/productos
GET    /api/v1/productos/{codigo}
PUT    /api/v1/productos/{codigo}
DELETE /api/v1/productos/{codigo}
```

### Órdenes (requiere ADMIN)

```
POST   /api/v1/ordenes
GET    /api/v1/ordenes
GET    /api/v1/ordenes/{id}
PUT    /api/v1/ordenes/{id}/estado
```

### Inventario (requiere ADMIN)

```
GET /api/v1/inventario/pdf?empresaNit={nit}
POST /api/v1/inventario/enviar-pdf
```

## Roles y Permisos

- **ADMIN**: Acceso completo a todas las operaciones
- **EXTERNO**: Solo lectura de empresas y productos

## Seguridad

- Contraseñas hasheadas con BCrypt (cost 12)
- Tokens JWT con expiración de 24 horas
- Login protegido con Google reCAPTCHA v3
- Validación de roles con `@PreAuthorize`
- Headers de seguridad configurados
- CORS configurado

## Manejo de Errores

Respuestas estandarizadas:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Descripción del error",
  "path": "/api/v1/empresas"
}
```

Códigos HTTP:
- `200`: Éxito
- `201`: Recurso creado
- `400`: Validación fallida
- `401`: No autenticado
- `403`: Sin permisos
- `404`: Recurso no encontrado
- `409`: Conflicto de negocio
- `422`: Entidad no procesable
- `500`: Error interno

Los errores críticos se registran automáticamente en `excepciones_sistema`.

## Testing

```bash
# Ejecutar todos los tests
mvn test

# Solo tests unitarios
mvn test -Dtest=*UnitTest

# Solo tests de integración
mvn test -Dtest=*IntegrationTest

# Con cobertura
mvn test jacoco:report
```

## Patrones de Diseño Aplicados

- **Hexagonal Architecture**: Separación de capas
- **Repository Pattern**: Acceso a datos
- **Factory Pattern**: Creación de entidades complejas
- **Strategy Pattern**: Validaciones de estados
- **Adapter Pattern**: Integraciones externas (reCAPTCHA, AWS)
- **Mapper Pattern**: Conversión de objetos (MapStruct)
- **DTO Pattern**: Transferencia de datos

## Principios SOLID

- **S**ingle Responsibility: Cada clase tiene una única responsabilidad
- **O**pen/Closed: Extensible sin modificar código existente
- **L**iskov Substitution: Interfaces bien definidas
- **I**nterface Segregation: Interfaces específicas
- **D**ependency Inversion: Dependencias hacia abstracciones

## Despliegue en AWS

### Opción 1: EC2

```bash
# Construir JAR
mvn clean package -DskipTests

# Transferir a EC2
scp target/enterprise-api-1.0.0.jar ec2-user@your-ec2-ip:/home/ec2-user/

# Conectar y ejecutar
ssh ec2-user@your-ec2-ip
nohup java -jar enterprise-api-1.0.0.jar &
```

### Opción 2: Elastic Beanstalk

```bash
eb init -p corretto-17 enterprise-api
eb create enterprise-api-env
eb deploy
```

### Opción 3: ECS (Docker)

```bash
docker build -t enterprise-api .
docker tag enterprise-api:latest your-ecr-repo/enterprise-api:latest
docker push your-ecr-repo/enterprise-api:latest
```

## Estructura de Datos Inicial

### Crear roles iniciales

```sql
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador con acceso completo'),
('EXTERNO', 'Usuario externo con acceso de solo lectura');
```

### Crear usuario administrador

```sql
-- Password: Admin123! (hasheado con BCrypt cost 12)
INSERT INTO usuarios (correo, password, rol_id, activo) VALUES
('admin@litethinking.com', '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYKKMQzzzm6', 1, true);
```

### Crear monedas

```sql
INSERT INTO monedas (codigo_iso, nombre, simbolo) VALUES
('COP', 'Peso Colombiano', '$'),
('USD', 'Dólar Estadounidense', '$'),
('EUR', 'Euro', '€');
```

### Crear estados de orden

```sql
INSERT INTO estados_orden (nombre) VALUES
('PENDIENTE'),
('PAGADA'),
('ANULADA');
```

## Credenciales de Prueba

**Usuario Administrador:**
- Correo: `admin@litethinking.com`
- Password: `Admin123!`

**Usuario Externo:**
- Correo: `externo@litethinking.com`
- Password: `Externo123!`

## Monitoreo y Logs

Los logs se almacenan en:
- Consola: Desarrollo
- Archivo: `logs/application.log` (rotación diaria)
- Base de datos: `excepciones_sistema` (errores críticos)


## Licencia

MIT License
