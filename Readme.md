# Sistema de Gestión Académica

## 📋 Descripción
API REST para la gestión académica desarrollada con Spring Boot y PostgreSQL. Permite gestionar profesores, cursos y estudiantes con operaciones CRUD completas.

## 🛠️ Tecnologías Utilizadas
- **Backend:** Spring Boot 3.5.4
- **Base de Datos:** PostgreSQL 17.4
- **ORM:** JPA/Hibernate
- **Build Tool:** Maven
- **Java:** JDK 17

## 📊 Estructura de la Base de Datos
- **profesores:** Información de los profesores
- **cursos:** Catálogo de cursos disponibles
- **estudiantes:** Registro de estudiantes
- **inscripciones:** Relación muchos a muchos entre estudiantes, cursos y profesores

## 🏗️ Arquitectura del Proyecto
```
src/main/java/com/gestionacademica/sistema_academico/
├── entity/          # Entidades JPA
├── repository/      # Interfaces de acceso a datos
├── service/         # Lógica de negocio
├── controller/      # Endpoints REST
└── SistemaAcademicoApplication.java
```

## 🚀 Configuración y Ejecución

### Prerrequisitos
- Java JDK 17 o superior
- PostgreSQL 17.4
- Maven 3.6+

### Configuración de Base de Datos
1. Crear base de datos: `gestion_academica_db`
2. Ejecutar scripts DDL ubicados en `schema.sql`
3. Configurar credenciales en `application.properties`

### Ejecución
```bash
./mvnw spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

## 📡 Endpoints API

### Profesores
- `GET /api/profesores` - Obtener todos los profesores
- `GET /api/profesores/{id}` - Obtener profesor por ID
- `POST /api/profesores` - Crear nuevo profesor
- `PUT /api/profesores/{id}` - Actualizar profesor
- `DELETE /api/profesores/{id}` - Eliminar profesor

### Cursos
- `GET /api/cursos` - Obtener todos los cursos
- `GET /api/cursos/{id}` - Obtener curso por ID
- `POST /api/cursos` - Crear nuevo curso
- `PUT /api/cursos/{id}` - Actualizar curso
- `DELETE /api/cursos/{id}` - Eliminar curso

### Estudiantes
- `GET /api/estudiantes` - Obtener todos los estudiantes
- `GET /api/estudiantes/{id}` - Obtener estudiante por ID
- `POST /api/estudiantes` - Crear nuevo estudiante
- `PUT /api/estudiantes/{id}` - Actualizar estudiante
- `DELETE /api/estudiantes/{id}` - Eliminar estudiante

## 📝 Ejemplos de Uso

### Crear Profesor
```json
POST /api/profesores
{
  "nombre": "Juan Carlos",
  "apellido": "Pérez García",
  "email": "juan.perez@universidad.edu",
  "telefono": "123456789",
  "especialidad": "Matemáticas",
  "fechaContratacion": "2024-01-15"
}
```

### Crear Curso
```json
POST /api/cursos
{
  "codigo": "MAT101",
  "nombre": "Matemáticas Básicas",
  "descripcion": "Curso introductorio de matemáticas",
  "creditos": 4,
  "horasSemanales": 6
}
```

### Crear Estudiante
```json
POST /api/estudiantes
{
  "carnet": "EST2024001",
  "nombre": "María José",
  "apellido": "González López",
  "email": "maria.gonzalez@estudiante.edu",
  "telefono": "555123456",
  "fechaNacimiento": "2000-05-15",
  "fechaIngreso": "2024-02-01"
}
```