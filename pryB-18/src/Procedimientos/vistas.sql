
 --vista para ver los todos los prestamos (vista Admin)
CREATE VIEW vista_prestamos_all_admin AS
SELECT p.prestamo_id, u.nombre AS estudiante, l.titulo AS libro, p.fecha_prestamo, p.fecha_vencimiento, d.multa, e.nombre AS estado
FROM prestamos p
INNER JOIN libros l ON p.libro_id = l.libro_id
INNER JOIN usuarios u ON p.usuario_id = u.usuario_id
INNER JOIN devoluciones d ON p.prestamo_id = d.prestamo_id
INNER JOIN estado e ON d.estados_id = e.estados_id;




-- vista Reporte a generar de todos los estudiantes 
-- muestra los préstamos con fecha de vencimiento anterior a la fecha actual 
-- y los que vencen en el día actual, siempre y cuando estén en estado pendiente.
CREATE VIEW vista_prestamos_estudiantes AS
SELECT u.usuario_id, u.nombre, u.email, u.telefono, u.tipo, pr.prestamo_id, pr.fecha_prestamo, pr.fecha_vencimiento, es.nombre AS estado_nombre, l.titulo AS libro_titulo
FROM usuarios u
JOIN estudiantes e ON u.usuario_id = e.usuario_id
JOIN prestamos pr ON u.usuario_id = pr.usuario_id
JOIN devoluciones d ON pr.prestamo_id = d.prestamo_id
JOIN estado es ON d.estados_id = es.estados_id
JOIN libros l ON pr.libro_id = l.libro_id
WHERE (pr.fecha_vencimiento < CURDATE() OR pr.fecha_vencimiento = CURDATE()) AND es.nombre = 'pendiente';

SELECT *
FROM vista_prestamos_estudiantes;

-- Vista que muestra los usuarios y contraseñas de los estudiantes y administradores
CREATE VIEW VistaUsuariosContraseñas AS
SELECT usuario, contraseña, 'Estudiante' AS tipo_usuario
FROM estudiantes
UNION ALL
SELECT usuario, contraseña, 'Administrador' AS tipo_usuario
FROM administradores;

