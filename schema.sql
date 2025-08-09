-- Sistema de Gestión Académica
-- Scripts DDL para PostgreSQL

-- Tabla Profesores (independiente)
CREATE TABLE profesores (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(15),
    especialidad VARCHAR(100),
    fecha_contratacion DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Cursos (independiente)
CREATE TABLE cursos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    creditos INTEGER NOT NULL,
    horas_semanales INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Estudiantes (independiente)
CREATE TABLE estudiantes (
    id SERIAL PRIMARY KEY,
    carnet VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(15),
    fecha_nacimiento DATE,
    fecha_ingreso DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla Inscripciones (dependiente - muchos a muchos)
CREATE TABLE inscripciones (
    id SERIAL PRIMARY KEY,
    estudiante_id INTEGER NOT NULL,
    curso_id INTEGER NOT NULL,
    profesor_id INTEGER NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    calificacion DECIMAL(4,2),
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_inscripcion_estudiante FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id),
    CONSTRAINT fk_inscripcion_curso FOREIGN KEY (curso_id) REFERENCES cursos(id),
    CONSTRAINT fk_inscripcion_profesor FOREIGN KEY (profesor_id) REFERENCES profesores(id),
    CONSTRAINT uq_estudiante_curso UNIQUE (estudiante_id, curso_id)
);

-- Índices para optimizar consultas
CREATE INDEX idx_profesores_email ON profesores(email);
CREATE INDEX idx_profesores_especialidad ON profesores(especialidad);
CREATE INDEX idx_cursos_codigo ON cursos(codigo);
CREATE INDEX idx_estudiantes_carnet ON estudiantes(carnet);
CREATE INDEX idx_estudiantes_email ON estudiantes(email);
CREATE INDEX idx_inscripciones_estudiante ON inscripciones(estudiante_id);
CREATE INDEX idx_inscripciones_curso ON inscripciones(curso_id);
CREATE INDEX idx_inscripciones_profesor ON inscripciones(profesor_id);