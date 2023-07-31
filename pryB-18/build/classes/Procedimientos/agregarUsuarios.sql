/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
DELIMITER //

CREATE PROCEDURE eliminar_usuario_con_validacion(IN usuario_id INT)
BEGIN
    DECLARE cantidad_prestamos INT;
    
    -- Consultar si existen préstamos asociados al usuario
    SELECT COUNT(*) INTO cantidad_prestamos FROM prestamos WHERE usuario_id = usuario_id;
    
    IF cantidad_prestamos > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No es posible eliminar el usuario. Tiene préstamos registrados.';
    ELSE
        -- Eliminar el usuario de todas las tablas correspondientes
        DELETE FROM usuarios WHERE usuario_id = usuario_id;
        
        SELECT 'El usuario ha sido eliminado con éxito.' AS mensaje;
    END IF;
END //

DELIMITER ;




-- Obtener el ID del usuario que realizará el préstamo
SET @usuario_id = (SELECT usuario_id FROM usuarios WHERE nombre = 'María Gómez' LIMIT 1);

-- Obtener el ID de un libro existente
SET @libro_id = (SELECT libro_id FROM libros LIMIT 1);

-- Insertar el préstamo en la tabla "prestamos"
INSERT INTO prestamos (usuario_id, libro_id, fecha_prestamo, fecha_vencimiento)
VALUES (@usuario_id, @libro_id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY));

