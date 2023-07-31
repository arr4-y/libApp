/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lenovo
 * Created: 15 jun. 2023
 */
CREATE TABLE Usuarios (
    id INT PRIMARY KEY,
    nombre VARCHAR(255),
    email VARCHAR(255),
    tipo VARCHAR(255),
    telefono VARCHAR(255)
    -- Otros campos relevantes para un usuario
);

CREATE TABLE Estudiantes (
    id INT PRIMARY KEY,
    usuario VARCHAR(255),
    contraseña VARCHAR(255),
    carrera VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Usuarios(id)
    -- Clave foránea que referencia al identificador del usuario en la tabla Usuarios
);

CREATE TABLE Administradores (
    id INT PRIMARY KEY,
    usuario VARCHAR(255),
    contraseña VARCHAR(255),
    FOREIGN KEY (id) REFERENCES Usuarios(id)
    -- Clave foránea que referencia al identificador del usuario en la tabla Usuarios
);
CREATE TABLE Libros (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(255),
    autor_id INT,
    editorial_id INT,
    anio_publicacion INT,
    -- Otros campos relevantes para un libro
    FOREIGN KEY (autor_id) REFERENCES Autores(id),
    -- Clave foránea que referencia al identificador del autor en la tabla Autores
    FOREIGN KEY (editorial_id) REFERENCES Editoriales(id)
    -- Clave foránea que referencia al identificador de la editorial en la tabla Editoriales
);

CREATE TABLE Prestamos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    libro_id INT,
    usuario_id INT,
    fecha_prestamo DATE,
    fecha_vencimiento DATE,
    -- Otros campos relevantes para un préstamo
    FOREIGN KEY (libro_id) REFERENCES Libros(id),
    -- Clave foránea que referencia al identificador del libro en la tabla Libros
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(id)
    -- Clave foránea que referencia al identificador del usuario en la tabla Usuarios
);

CREATE TABLE Editoriales (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    -- Otros campos relevantes para una editorial
);

CREATE TABLE Autores (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255),
    -- Otros campos relevantes para un autor
);

CREATE TABLE Devoluciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    prestamo_id INT,
    fecha_devolucion DATE,
    estado VARCHAR(255),
    multa DECIMAL(10, 2),
    -- Otros campos relacionados con la devolución
    FOREIGN KEY (prestamo_id) REFERENCES Prestamos(id)
    -- Clave foránea que referencia al identificador del préstamo en la tabla Prestamos
);
ALTER TABLE devoluciones
MODIFY COLUMN estado ENUM('devuelto', 'por devolver') DEFAULT 'por devolver';


