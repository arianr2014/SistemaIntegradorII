# Sistema Integrador II - Plataforma NovaSalud (Citas Médicas & Farmacia)

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-10-F09819?logo=eclipse&logoColor=white)](https://jakarta.ee/)
[![Apache Maven](https://img.shields.io/badge/Maven-3.8%2B-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Apache Tomcat](https://img.shields.io/badge/Tomcat-10.1%2B-F8DC75?logo=apachetomcat&logoColor=black)](https://tomcat.apache.org/)

---

## 1. Descripción General del Proyecto

**NovaSalud** es una solución integral orientada a la gestión hospitalaria y farmacéutica, desarrollada bajo una **arquitectura desacoplada y orientada a servicios (SOA / REST)**. El sistema está estructurado como un proyecto padre multi-módulo en **Apache Maven** y se despliega sobre **Apache Tomcat 10.1+** utilizando la especificación **Jakarta EE 10**.

El repositorio se compone de 4 módulos independientes:

```text
SistemaIntegradorII/
├── pom.xml                           # POM Padre Multi-Módulo
├── database/                         # Scripts de Base de Datos MySQL
│   ├── v321/                         # Base de Datos: Citas Médicas (bd_citasmedicas_v321)
│   └── Farmacia/                     # Base de Datos: Farmacia (bd_famacia)
├── novasalud-citas-api/              # [Backend] REST API Citas Médicas (Jakarta REST, JWT, HikariCP)
├── novasalud-citas-web/              # [Frontend] Web App Citas Médicas (JSP, JSTL, Servlets MVC)
├── novasalud-farmacia-api/           # [Backend] REST API Farmacia (Jakarta REST, JWT, HikariCP)
└── novasalud-farmacia-web/           # [Frontend] Web App Farmacia (JSP, JSTL, Servlets MVC)
```

---

## 2. Requisitos Previos del Entorno

Asegurarse de tener instaladas las siguientes herramientas en el sistema operativo:

- **Java Development Kit (JDK):** Versión 17 o superior.
- **Apache Maven:** Versión 3.8 o superior.
- **MySQL Server:** Versión 8.0 o superior (ejecutándose en el puerto por defecto `3306`).
- **Apache Tomcat:** Versión 10.1+ (imprescindible para compatibilidad con el paquete `jakarta.*` de Jakarta EE 10).
- **Git:** Para control de versiones.

---

## 3. Guía de Implementación de la Base de Datos

El sistema utiliza **dos bases de datos independientes** en MySQL: una para el módulo de Citas Médicas y otra para el módulo de Farmacia. Se deben ejecutar los scripts en el orden indicado a continuación.

### 3.1. Base de Datos: Citas Médicas (`bd_citasmedicas_v321`)

Ubicación de scripts: carpeta `database/v321/`

#### Orden de ejecución:

1. **Estructura y Datos Base:**
   - **Archivo:** `database/v321/00_instalacion_completa_bd_citasmedicas_v321.sql`
   - **Descripción:** Crea la base de datos `bd_citasmedicas_v321`, tablas maestras (pacientes, médicos, especialidades, citas, pagos, historia clínica, etc.) e inserta datos iniciales.
2. **Procedimientos Almacenados:**
   - **Archivo:** `database/v321/01_stored_procedures_completos_v321.sql`
   - **Descripción:** Registra todos los Stored Procedures requeridos para transacciones y lógica de negocio.
3. **Usuario de Base de Datos y Permisos:**
   - **Archivo:** `database/v321/bd_usuario.sql`
   - **Descripción:** Crea el usuario MySQL `usuario_citas` con clave `ISO/IEC27001` y asigna privilegios completos sobre `bd_citasmedicas_v321`.

#### Ejecución desde consola MySQL:
```bash
mysql -u root -p < database/v321/00_instalacion_completa_bd_citasmedicas_v321.sql
mysql -u root -p bd_citasmedicas_v321 < database/v321/01_stored_procedures_completos_v321.sql
mysql -u root -p < database/v321/bd_usuario.sql
```

---

### 3.2. Base de Datos: Farmacia (`bd_famacia`)

Ubicación de scripts: carpeta `database/Farmacia/`

#### Orden de ejecución:

1. **Estructura de Tablas:**
   - **Archivo:** `database/Farmacia/bd_farmacia_v1.sql`
   - **Descripción:** Crea la base de datos `bd_famacia` y las tablas de categorías, medicamentos, proveedores, lotes, inventarios, usuarios y ventas.
2. **Procedimientos Almacenados:**
   - **Archivo:** `database/Farmacia/bd_farmacia_sp_v1.sql`
   - **Descripción:** Crea los Stored Procedures para gestión de stock, alertas de caducidad, registro de ventas y kardex.
3. **Datos de Inicialización:**
   - **Archivo:** `database/Farmacia/02_insert_data_farmacia_v1.sql`
   - **Descripción:** Inserta catálogos de medicamentos, proveedores, usuarios de farmacia y stock de prueba.
4. **Usuario de Base de Datos y Permisos:**
   - **Archivo:** `database/Farmacia/bd_usuario.sql`
   - **Descripción:** Crea el usuario MySQL `usuario_farmacia` con clave `Farmacia2026*` y le otorga privilegios sobre `bd_famacia`.

#### Ejecución desde consola MySQL:
```bash
mysql -u root -p < database/Farmacia/bd_farmacia_v1.sql
mysql -u root -p bd_famacia < database/Farmacia/bd_farmacia_sp_v1.sql
mysql -u root -p bd_famacia < database/Farmacia/02_insert_data_farmacia_v1.sql
mysql -u root -p < database/Farmacia/bd_usuario.sql
```

---

## 4. Configuración del Acceso a Base de Datos

Cada módulo Backend (`API`) cuenta con un archivo `db.properties` en `src/main/resources/` para controlar el pool de conexiones **HikariCP** y las credenciales JDBC.

### 4.1. Configuración de Citas API
- **Archivo:** `novasalud-citas-api/src/main/resources/db.properties`

```properties
# Configuración de Base de Datos - NovaSalud Citas API
db.url=jdbc:mysql://localhost:3306/bd_citasmedicas_v321?useSSL=false&allowPublicKeyRetrieval=true&noAccessToProcedureBodies=true&serverTimezone=UTC
db.user=usuario_citas
db.password=ISO/IEC27001
db.driver=com.mysql.cj.jdbc.Driver

# Parámetros del Pool HikariCP
db.pool.maximumPoolSize=10
db.pool.minimumIdle=2
db.pool.idleTimeout=30000
db.pool.connectionTimeout=10000
db.pool.maxLifetime=1800000

# Clave Secreta para Firma JWT (HMAC-SHA256)
jwt.secret=NovaSaludSystemSecretKeyHMAC256BitsLongForSecureJwtTokenSigning2026UTP
```

### 4.2. Configuración de Farmacia API
- **Archivo:** `novasalud-farmacia-api/src/main/resources/db.properties`

```properties
# Configuración de Base de Datos - NovaSalud Farmacia API
db.url=jdbc:mysql://localhost:3306/bd_famacia?useSSL=false&allowPublicKeyRetrieval=true&noAccessToProcedureBodies=true&serverTimezone=UTC
db.user=usuario_farmacia
db.password=Farmacia2026*
db.driver=com.mysql.cj.jdbc.Driver

# Parámetros del Pool HikariCP
db.pool.maximumPoolSize=10
db.pool.minimumIdle=2
db.pool.idleTimeout=30000
db.pool.connectionTimeout=10000
db.pool.maxLifetime=1800000

# Clave Secreta para Firma JWT (HMAC-SHA256)
jwt.secret=NovaSaludFarmaciaSystemSecretKeyHMAC256BitsLongForSecureJwtTokenSigning2026UTP
```

> **Nota:** La propiedad `noAccessToProcedureBodies=true` y `allowPublicKeyRetrieval=true` garantizan que los usuarios de base de datos puedan invocar los Stored Procedures y autenticarse con MySQL 8 sin requerir permisos de `SUPER` o lectura del catálogo `mysql.proc`.

---

## 5. Configuración de Endpoints y Comunicación Frontend-Backend

Los módulos Frontend Web consumen los servicios Backend a través de peticiones HTTP REST con tokens **Bearer JWT** gestionados por la clase utilitaria `ApiClient.java`.

La URL base de cada API se define en el archivo `api.properties` dentro de cada proyecto Web:

### 5.1. Conexión de Citas Web hacia Citas API
- **Archivo:** `novasalud-citas-web/src/main/resources/api.properties`
```properties
# URL Base de la API Backend de NovaSalud Citas
api.base.url=http://localhost:8080/novasalud-citas-api/api
```

### 5.2. Conexión de Farmacia Web hacia Farmacia API
- **Archivo:** `novasalud-farmacia-web/src/main/resources/api.properties`
```properties
# URL Base de la API Backend de NovaSalud Farmacia
api.base.url=http://localhost:8080/novasalud-farmacia-api/api
```

> **Importante si cambias de puerto o dominio:** Si tu Apache Tomcat se ejecuta en un puerto distinto a `8080` (por ejemplo `8088` o `9090`) o en un servidor remoto, debes actualizar el valor de `api.base.url` en los archivos mencionados antes de compilar.

---

## 6. Compilación y Generación de Paquetes WAR

El proyecto cuenta con un `pom.xml` padre que orquesta la compilación de los 4 módulos de forma simultánea.

### 6.1. Compilación Completa (Recomendado)
Desde la raíz del repositorio (`SistemaIntegradorII/`), ejecuta:

```bash
mvn clean package
```

Esto generará los 4 empaquetados `.war` listos para desplegar:
- `novasalud-citas-api/target/novasalud-citas-api.war`
- `novasalud-citas-web/target/novasalud-citas-web.war`
- `novasalud-farmacia-api/target/novasalud-farmacia-api.war`
- `novasalud-farmacia-web/target/novasalud-farmacia-web.war`

### 6.2. Compilación Individual por Módulo
Si solo necesitas compilar un módulo en específico:
```bash
# Compilar únicamente Citas API
mvn clean package -pl novasalud-citas-api

# O entrar a la carpeta del módulo
cd novasalud-farmacia-web
mvn clean package
```

---

## 7. Despliegue y URLs de Acceso

1. Copiar los archivos `.war` generados a la carpeta `webapps/` de tu instalación de **Apache Tomcat 10.1+**:
   ```bash
   cp novasalud-citas-api/target/novasalud-citas-api.war /opt/tomcat/webapps/
   cp novasalud-citas-web/target/novasalud-citas-web.war /opt/tomcat/webapps/
   cp novasalud-farmacia-api/target/novasalud-farmacia-api.war /opt/tomcat/webapps/
   cp novasalud-farmacia-web/target/novasalud-farmacia-web.war /opt/tomcat/webapps/
   ```
2. Iniciar Tomcat (`bin/startup.sh` en Linux o `bin/startup.bat` en Windows).

### Rutas de Acceso en el Navegador:

| Módulo / Aplicación | URL de Acceso | Descripción |
|---|---|---|
| **Citas Médicas Web (Frontend)** | `http://localhost:8080/novasalud-citas-web/` | Portal web para citas, pacientes, médicos y triaje. |
| **Citas Médicas API (Backend)** | `http://localhost:8080/novasalud-citas-api/api` | Endpoints REST de Citas Médicas. |
| **Swagger UI - Citas API** | `http://localhost:8080/novasalud-citas-api/swagger` | Documentación interactiva OpenAPI 3.0 de Citas. |
| **Farmacia Web (Frontend)** | `http://localhost:8080/novasalud-farmacia-web/` | Portal web de dispensación, ventas e inventario. |
| **Farmacia API (Backend)** | `http://localhost:8080/novasalud-farmacia-api/api` | Endpoints REST del módulo Farmacia. |
| **Swagger UI - Farmacia API** | `http://localhost:8080/novasalud-farmacia-api/swagger` | Documentación interactiva OpenAPI 3.0 de Farmacia. |

---

## 8. Credenciales del Sistema

> **Credencial de Administrador:** La contraseña para iniciar sesión como Administrador en el sistema es **`123456`**.

### 8.1. Usuarios Web para Pruebas

#### Módulo de Citas Médicas:
| Rol | Usuario | Contraseña | Perfil |
|---|---|---|---|
| **Administrador** | `administrador` | `123456` | Acceso a configuración, tarifarios, auditoría y reportes. |
| **Recepcionista** | `recepcionista` | `123456` | Programación de citas, registro de pacientes y cobros en caja. |
| **Médico** | `doctor` | `123456` | Agenda médica personal, registro de triaje, diagnóstico y recetas. |

#### Módulo de Farmacia:
| Rol | Usuario | Contraseña | Perfil |
|---|---|---|---|
| **Administrador** | `admin` | `123456` | Gestión total, reportes de stock y kardex. *(Ver script `database/Farmacia/updatecontraseña.sql`)* |
| **Químico Farmacéutico** | `qf_martinez` | `123456` | Validación de recetas médicas y control de inventario/lotes. |
| **Técnico de Farmacia** | `cajero01` | `123456` | Venta por ventanilla y cobro de medicamentos. |

---

### 8.2. Usuarios de Base de Datos MySQL

| Base de Datos | Usuario MySQL | Contraseña | Host Permitido | Permisos Asignados |
|---|---|---|---|---|
| `bd_citasmedicas_v321` | `usuario_citas` | `ISO/IEC27001` | `localhost` | `ALL PRIVILEGES ON bd_citasmedicas_v321.*` |
| `bd_famacia` | `usuario_farmacia` | `Farmacia2026*` | `localhost` | `ALL PRIVILEGES ON bd_famacia.*` |

---

## 9. Arquitectura Técnica y Seguridad

- **Seguridad stateless con JWT:** Los endpoints protegidos validan el token en la cabecera HTTP `Authorization: Bearer <jwt-token>`.
- **Cifrado de contraseñas:** Uso de **BCrypt** para almacenamiento seguro de credenciales de usuario.
- **Pool HikariCP:** Optimización de conexiones hacia MySQL con auto-reconexión y limpieza de conexiones ociosas.
- **Protección CSRF & CORS:** Filtros configurados para prevenir peticiones cruzadas no autorizadas.
