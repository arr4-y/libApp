
--modificar de estudiante a admin o de admin a estudiante
DELIMITER //
CREATE PROCEDURE actualizar_tipo_usuario(
    IN p_id INT,
    IN p_nombre VARCHAR(100),
    IN p_email VARCHAR(100),
    IN p_tipo_usuario ENUM('Administrador', 'Estudiante'),
    IN p_usuario VARCHAR(100),
    IN p_contraseña VARCHAR(100),
    IN p_telefono VARCHAR(20),
    IN p_carrera VARCHAR(100)
)
BEGIN
    DECLARE tipo_actual VARCHAR(20);

    -- Obtener el tipo actual del usuario
    SELECT tipo INTO tipo_actual FROM usuarios WHERE id = p_id;

    -- Actualizar la tabla usuarios
    UPDATE usuarios
    SET nombre = p_nombre,
        email = p_email,
        tipo = p_tipo_usuario,
        telefono = p_telefono
    WHERE id = p_id;

    IF tipo_actual = 'Estudiante' AND p_tipo_usuario = 'Administrador' THEN
        -- Insertar en la tabla administradores si no existe
        INSERT INTO administradores (id, usuario, contraseña)
        VALUES (p_id, p_usuario, p_contraseña)
        ON DUPLICATE KEY UPDATE
            usuario = p_usuario,
            contraseña = p_contraseña;

        -- Eliminar de la tabla estudiantes si existe
        DELETE FROM estudiantes WHERE id = p_id;

    ELSEIF tipo_actual = 'Administrador' AND p_tipo_usuario = 'Estudiante' THEN
        -- Insertar en la tabla estudiantes si no existe
        INSERT INTO estudiantes (id, usuario, contraseña, carrera)
        VALUES (p_id, p_usuario, p_contraseña, p_carrera)
        ON DUPLICATE KEY UPDATE
            usuario = p_usuario,
            contraseña = p_contraseña,
            carrera = p_carrera;

        -- Eliminar de la tabla administradores si existe
        DELETE FROM administradores WHERE id = p_id;
    ELSE
        -- El tipo de usuario no ha cambiado, actualizar los datos en la tabla correspondiente
        IF p_tipo_usuario = 'Estudiante' THEN
            -- Actualizar la tabla estudiantes
            UPDATE estudiantes
            SET usuario = p_usuario,
                contraseña = p_contraseña,
                carrera = p_carrera
            WHERE id = p_id;
        ELSEIF p_tipo_usuario = 'Administrador' THEN
            -- Actualizar la tabla administradores
            UPDATE administradores
            SET usuario = p_usuario,
                contraseña = p_contraseña
            WHERE id = p_id;
        END IF;
    END IF;
END //


DELIMITER ;


--PROCEDIMIENTO PARA ELIMINAR USUARIO
DELIMITER //
CREATE PROCEDURE eliminar_usuario(
    IN p_id INT
)
BEGIN
    -- Eliminar de la tabla estudiantes si existe
    DELETE FROM estudiantes WHERE id = p_id;

    -- Eliminar de la tabla administradores si existe
    DELETE FROM administradores WHERE id = p_id;

    -- Eliminar de la tabla usuarios
    DELETE FROM usuarios WHERE id = p_id;
END //
DELIMITER ;




--PROCEDIMIENTO PARA INSERTAR PRESTAMOS Y DEVOLUCIONES
DELIMITER @@
Drop PROCEDURE if EXISTS spRegistra_prestamo @@
CREATE PROCEDURE spRegistra_prestamo(idLibro int, idUsu int, fechPre date, fechVen date)
BEGIN
DECLARE nro int;
insert into prestamos VALUES (null,idLibro,idUsu,fechPre,fechVen);
SELECT ifnull(max(prestamo_id),0) into nro from prestamos;
INSERT into devoluciones VALUES (null,nro,fechVen,1,0);
end@@




 

 



DELIMITER //
CREATE PROCEDURE actualizarFechasDevolucion(IN prestamoId INT, IN nuevaFechaDevolucion DATE)
BEGIN
     UPDATE prestamos SET fecha_vencimiento = nuevaFechaDevolucion WHERE prestamo_id = prestamoId;
    UPDATE devoluciones SET fecha_devolucion = nuevaFechaDevolucion WHERE prestamo_id = prestamoId;
    SELECT 'Las fechas de devolución han sido actualizadas con éxito.';
END //
DELIMITER ;


 DELIMITER //

CREATE PROCEDURE eliminar_usuario_con_validacion(
    IN p_usuario_id INT
)
BEGIN
    DECLARE cantidad_prestamos INT;

    -- Verificar si el usuario tiene préstamos registrados
    SELECT COUNT(*) INTO cantidad_prestamos FROM prestamos WHERE usuario_id = p_usuario_id;

    -- Si hay préstamos registrados, no se puede eliminar el usuario
    IF cantidad_prestamos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No es posible eliminar el usuario. Tiene préstamos registrados.';
    ELSE
        -- Eliminar de la tabla estudiantes si existe
        DELETE FROM estudiantes WHERE usuario_id = p_usuario_id;

        -- Eliminar de la tabla administradores si existe
        DELETE FROM administradores WHERE usuario_id = p_usuario_id;

        -- Eliminar de la tabla usuarios
        DELETE FROM usuarios WHERE usuario_id = p_usuario_id;
    END IF;
END //

DELIMITER ;