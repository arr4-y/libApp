-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 17-07-2023 a las 23:57:09
-- Versión del servidor: 10.4.25-MariaDB
-- Versión de PHP: 7.4.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bdbiblioteca2`
--

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`` PROCEDURE `actualizarEstadoDevolucion` (IN `p_devolucion_id` INT)   BEGIN
    -- Actualizar el estado de la devolución a "Devuelto"
    UPDATE devoluciones
    SET estados_id = (SELECT estados_id FROM estado WHERE nombre = 'Devuelto')
    WHERE devolucion_id = p_devolucion_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizarFechasDevolucion` (IN `prestamoId` INT, IN `nuevaFechaDevolucion` DATE, IN `estado` INT)   BEGIN
     UPDATE prestamos SET fecha_vencimiento = nuevaFechaDevolucion WHERE prestamo_id = prestamoId;
    UPDATE devoluciones SET fecha_devolucion = nuevaFechaDevolucion WHERE prestamo_id = prestamoId;
    UPDATE devoluciones SET estados_id = estado WHERE prestamo_id = prestamoId;
    SELECT 'Las fechas de devolución han sido actualizadas con éxito.';
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `actualizar_tipo_usuario` (IN `p_id` INT, IN `p_nombre` VARCHAR(100), IN `p_email` VARCHAR(100), IN `p_tipo_usuario` ENUM('Administrador','Estudiante'), IN `p_usuario` VARCHAR(100), IN `p_contraseña` VARCHAR(100), IN `p_telefono` VARCHAR(20), IN `p_carrera` VARCHAR(100))   BEGIN
    DECLARE tipo_actual VARCHAR(20);

    -- Obtener el tipo actual del usuario
    SELECT tipo INTO tipo_actual FROM usuarios WHERE usuario_id = p_id;

    -- Actualizar la tabla usuarios
    UPDATE usuarios
    SET nombre = p_nombre,
        email = p_email,
        tipo = p_tipo_usuario,
        telefono = p_telefono
    WHERE usuario_id = p_id;

    IF tipo_actual = 'Estudiante' AND p_tipo_usuario = 'Administrador' THEN
        -- Insertar en la tabla administradores si no existe
        INSERT INTO administradores (usuario_id, usuario, contraseña)
        VALUES (p_id, p_usuario, p_contraseña)
        ON DUPLICATE KEY UPDATE
            usuario = p_usuario,
            contraseña = p_contraseña;

        -- Eliminar de la tabla estudiantes si existe
        DELETE FROM estudiantes WHERE usuario_id = p_id;

    ELSEIF tipo_actual = 'Administrador' AND p_tipo_usuario = 'Estudiante' THEN
        -- Insertar en la tabla estudiantes si no existe
        INSERT INTO estudiantes (usuario_id, usuario, contraseña, carrera)
        VALUES (p_id, p_usuario, p_contraseña, p_carrera)
        ON DUPLICATE KEY UPDATE
            usuario = p_usuario,
            contraseña = p_contraseña,
            carrera = p_carrera;

        -- Eliminar de la tabla administradores si existe
        DELETE FROM administradores WHERE usuario_id = p_id;
    ELSE
        -- El tipo de usuario no ha cambiado, actualizar los datos en la tabla correspondiente
        IF p_tipo_usuario = 'Estudiante' THEN
            -- Actualizar la tabla estudiantes
            UPDATE estudiantes
            SET usuario = p_usuario,
                contraseña = p_contraseña,
                carrera = p_carrera
            WHERE usuario_id = p_id;
        ELSEIF p_tipo_usuario = 'Administrador' THEN
            -- Actualizar la tabla administradores
            UPDATE administradores
            SET usuario = p_usuario,
                contraseña = p_contraseña
            WHERE usuario_id = p_id;
        END IF;
    END IF;
END$$

CREATE DEFINER=`` PROCEDURE `AumentarMulta` ()   BEGIN
    DECLARE fecha_actual DATE;
    DECLARE fecha_devolucion DATE;
    DECLARE multa_actual DECIMAL(10, 2);
    
    SET fecha_actual = CURRENT_DATE();
    
    -- Obtener los registros de devoluciones pendientes
    SELECT fecha_devolucion, multa INTO fecha_devolucion, multa_actual
    FROM devoluciones
    WHERE estado = 'pendiente';
    
    IF fecha_actual > fecha_devolucion THEN
        -- Incrementar la multa en 5 soles
        SET multa_actual = multa_actual + 5;
        
        -- Actualizar la multa en la tabla de devoluciones
        UPDATE devoluciones
        SET multa = multa_actual
        WHERE estado = 'pendiente';
    END IF;
END$$

CREATE DEFINER=`` PROCEDURE `eliminar_usuario_con_validacion` (IN `p_usuario_id` INT)   BEGIN
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
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `spRegistra_prestamo` (IN `idLibro` INT, IN `idUsu` INT, IN `fechPre` DATE, IN `fechVen` DATE)   BEGIN
DECLARE nro int;
insert into prestamos VALUES (null,idLibro,idUsu,fechPre,fechVen);
SELECT ifnull(max(prestamo_id),0) into nro from prestamos;
INSERT into devoluciones VALUES (null,nro,fechVen,0,1);
end$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administradores`
--

CREATE TABLE `administradores` (
  `usuario_id` int(11) NOT NULL,
  `usuario` varchar(100) NOT NULL,
  `contraseña` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `administradores`
--

INSERT INTO `administradores` (`usuario_id`, `usuario`, `contraseña`) VALUES
(67, 'userAdmin', '123'),
(72, 'paco', '123');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `autores`
--

CREATE TABLE `autores` (
  `autor_id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `autores`
--

INSERT INTO `autores` (`autor_id`, `nombre`) VALUES
(13, 'John Smith'),
(14, 'María Antonieta'),
(15, 'Margaret Mitchell'),
(16, 'Alcott, Louisa May '),
(21, 'F. Scott Fitzgerald'),
(22, 'Gabriel García Márquez'),
(23, 'F. Scott Fitzgerald'),
(24, 'Gabriel García Márquez'),
(25, 'Miguel de Cervantes'),
(26, 'Jane Austen'),
(27, 'George Orwell'),
(28, 'Antoine de Saint-Exupéry');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `devoluciones`
--

CREATE TABLE `devoluciones` (
  `devolucion_id` int(11) NOT NULL,
  `prestamo_id` int(11) DEFAULT NULL,
  `fecha_devolucion` date DEFAULT NULL,
  `multa` decimal(10,2) DEFAULT NULL,
  `estados_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `devoluciones`
--

INSERT INTO `devoluciones` (`devolucion_id`, `prestamo_id`, `fecha_devolucion`, `multa`, `estados_id`) VALUES
(5, 7, '2023-07-19', '0.00', 1),
(6, 8, '2023-07-16', '5.00', 1),
(7, 9, '2023-07-17', '0.00', 1),
(8, 10, '2023-07-20', '0.00', 1),
(9, 11, '2023-07-16', '5.00', 1),
(10, 12, '2023-07-22', '0.00', 1),
(11, 17, '2023-07-22', '0.00', 1),
(12, 18, '2023-07-22', '0.00', 1);

--
-- Disparadores `devoluciones`
--
DELIMITER $$
CREATE TRIGGER `tgAumentarMulta` BEFORE UPDATE ON `devoluciones` FOR EACH ROW BEGIN
    IF NEW.fecha_devolucion > OLD.fecha_devolucion THEN
        SET NEW.multa = NEW.multa + 5;
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `editoriales`
--

CREATE TABLE `editoriales` (
  `editorial_id` int(11) NOT NULL,
  `nombre` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `editoriales`
--

INSERT INTO `editoriales` (`editorial_id`, `nombre`) VALUES
(14, 'Editorial ABC'),
(16, 'Planeta'),
(17, 'Alba'),
(18, 'B de Bolsillo'),
(19, 'Alfaguara'),
(22, 'Scribner'),
(23, 'Sudamericana'),
(24, 'Francisco de Robles'),
(25, 'T. Egerton, Whitehall'),
(26, 'Gallimard'),
(27, 'Secker & Warburg');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estado`
--

CREATE TABLE `estado` (
  `estados_id` int(11) NOT NULL,
  `nombre` varchar(30) DEFAULT NULL,
  `descripcion` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `estado`
--

INSERT INTO `estado` (`estados_id`, `nombre`, `descripcion`) VALUES
(1, 'pendiente', 'A tiempo de devolver'),
(2, 'devuelto', 'Devuelto exitosamente');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estudiantes`
--

CREATE TABLE `estudiantes` (
  `usuario_id` int(11) NOT NULL,
  `usuario` varchar(100) NOT NULL,
  `contraseña` varchar(100) NOT NULL,
  `carrera` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `estudiantes`
--

INSERT INTO `estudiantes` (`usuario_id`, `usuario`, `contraseña`, `carrera`) VALUES
(69, 'user', 'pass', 'civil'),
(71, 'alber789', '123', 'ing. software'),
(106, 'Marmez', '34761407', 'Ingeniería'),
(107, 'Carpez', '25482086', 'Arquitectura'),
(108, 'Laurez', '23126214', 'Medicina'),
(109, 'Pabuez', '39184815', 'Economía'),
(110, 'Sofnez', '26545436', 'Psicología'),
(112, 'Valcía', '96227396', 'Comunicación');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libros`
--

CREATE TABLE `libros` (
  `libro_id` int(11) NOT NULL,
  `titulo` varchar(255) DEFAULT NULL,
  `autor_id` int(11) DEFAULT NULL,
  `editorial_id` int(11) DEFAULT NULL,
  `cantidad` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `libros`
--

INSERT INTO `libros` (`libro_id`, `titulo`, `autor_id`, `editorial_id`, `cantidad`) VALUES
(18, 'Lo que el viento se llevó', 15, 18, 5),
(19, 'Mujercitas', 16, 19, 5),
(26, 'Cien Años de Soledad', 22, 23, 7),
(27, 'Don Quijote de la Mancha', 25, 24, 3),
(28, '1984', 27, 27, 2),
(29, 'El principito', 28, 26, 9),
(30, 'Orgullo y prejuicio', 26, 25, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `prestamos`
--

CREATE TABLE `prestamos` (
  `prestamo_id` int(11) NOT NULL,
  `libro_id` int(11) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `fecha_prestamo` date DEFAULT NULL,
  `fecha_vencimiento` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `prestamos`
--

INSERT INTO `prestamos` (`prestamo_id`, `libro_id`, `usuario_id`, `fecha_prestamo`, `fecha_vencimiento`) VALUES
(7, 18, 69, '2023-07-13', '2023-07-19'),
(8, 19, 69, '2023-07-13', '2023-07-16'),
(9, 18, 69, '2023-07-13', '2023-07-17'),
(10, 19, 69, '2023-07-13', '2023-07-20'),
(11, 19, 71, '2023-07-15', '2023-07-16'),
(12, 18, 71, '2023-07-15', '2023-07-22'),
(16, 18, 106, '2023-07-15', '2023-07-22'),
(17, 19, 106, '2023-07-15', '2023-07-22'),
(18, 19, 71, '2023-07-15', '2023-07-22');

--
-- Disparadores `prestamos`
--
DELIMITER $$
CREATE TRIGGER `tgPrestamo` AFTER INSERT ON `prestamos` FOR EACH ROW UPDATE libros set cantidad=cantidad-1 where libro_id=new.libro_id
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `usuario_id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `tipo` enum('Administrador','Estudiante') DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`usuario_id`, `nombre`, `email`, `tipo`, `telefono`) VALUES
(67, 'Carlos Miran', 'amd@gmail.com', 'Administrador', '978654321'),
(69, 'Luis Campos', 'est@gmail.com', 'Estudiante', '951357456'),
(71, 'Albert Castillo', 'alber987@gmail.com', 'Estudiante', '987654123'),
(72, 'Paco Morán', 'paco@gmail.com', 'Administrador', '95142368'),
(106, 'María Gómez', 'maria.gomez@example.com', 'Estudiante', '987654321'),
(107, 'Carlos López', 'carlos.lopez@example.com', 'Estudiante', '987654322'),
(108, 'Laura Ramírez', 'laura.ramirez@example.com', 'Estudiante', '987654323'),
(109, 'Pablo Rodríguez', 'pablo.rodriguez@example.com', 'Estudiante', '987654324'),
(110, 'Sofía Martínez', 'sofia.martinez@example.com', 'Estudiante', '987654325'),
(112, 'Valentina García', 'valentina.garcia@example.com', 'Estudiante', '987654328');

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vistausuarioscontraseñas`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vistausuarioscontraseñas` (
`usuario` varchar(100)
,`contraseña` varchar(100)
,`tipo_usuario` varchar(13)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_prestamos_all_admin2`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_prestamos_all_admin2` (
`prestamo_id` int(11)
,`estudiante` varchar(100)
,`libro` varchar(255)
,`fecha_prestamo` date
,`fecha_vencimiento` date
,`multa` decimal(10,2)
,`estado` varchar(30)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `vista_prestamos_estudiantes`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `vista_prestamos_estudiantes` (
`usuario_id` int(11)
,`nombre` varchar(100)
,`email` varchar(100)
,`telefono` varchar(20)
,`tipo` enum('Administrador','Estudiante')
,`prestamo_id` int(11)
,`fecha_prestamo` date
,`fecha_vencimiento` date
,`estado_nombre` varchar(30)
,`libro_titulo` varchar(255)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `vistausuarioscontraseñas`
--
DROP TABLE IF EXISTS `vistausuarioscontraseñas`;

CREATE ALGORITHM=UNDEFINED DEFINER=`` SQL SECURITY DEFINER VIEW `vistausuarioscontraseñas`  AS SELECT `estudiantes`.`usuario` AS `usuario`, `estudiantes`.`contraseña` AS `contraseña`, 'Estudiante' AS `tipo_usuario` FROM `estudiantes` union all select `administradores`.`usuario` AS `usuario`,`administradores`.`contraseña` AS `contraseña`,'Administrador' AS `tipo_usuario` from `administradores`  ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_prestamos_all_admin2`
--
DROP TABLE IF EXISTS `vista_prestamos_all_admin2`;

CREATE ALGORITHM=UNDEFINED DEFINER=`` SQL SECURITY DEFINER VIEW `vista_prestamos_all_admin2`  AS SELECT `p`.`prestamo_id` AS `prestamo_id`, `u`.`nombre` AS `estudiante`, `l`.`titulo` AS `libro`, `p`.`fecha_prestamo` AS `fecha_prestamo`, `p`.`fecha_vencimiento` AS `fecha_vencimiento`, `d`.`multa` AS `multa`, `e`.`nombre` AS `estado` FROM ((((`prestamos` `p` join `libros` `l` on(`p`.`libro_id` = `l`.`libro_id`)) join `usuarios` `u` on(`p`.`usuario_id` = `u`.`usuario_id`)) join `devoluciones` `d` on(`p`.`prestamo_id` = `d`.`prestamo_id`)) join `estado` `e` on(`d`.`estados_id` = `e`.`estados_id`))  ;

-- --------------------------------------------------------

--
-- Estructura para la vista `vista_prestamos_estudiantes`
--
DROP TABLE IF EXISTS `vista_prestamos_estudiantes`;

CREATE ALGORITHM=UNDEFINED DEFINER=`` SQL SECURITY DEFINER VIEW `vista_prestamos_estudiantes`  AS SELECT `u`.`usuario_id` AS `usuario_id`, `u`.`nombre` AS `nombre`, `u`.`email` AS `email`, `u`.`telefono` AS `telefono`, `u`.`tipo` AS `tipo`, `pr`.`prestamo_id` AS `prestamo_id`, `pr`.`fecha_prestamo` AS `fecha_prestamo`, `pr`.`fecha_vencimiento` AS `fecha_vencimiento`, `es`.`nombre` AS `estado_nombre`, `l`.`titulo` AS `libro_titulo` FROM (((((`usuarios` `u` join `estudiantes` `e` on(`u`.`usuario_id` = `e`.`usuario_id`)) join `prestamos` `pr` on(`u`.`usuario_id` = `pr`.`usuario_id`)) join `devoluciones` `d` on(`pr`.`prestamo_id` = `d`.`prestamo_id`)) join `estado` `es` on(`d`.`estados_id` = `es`.`estados_id`)) join `libros` `l` on(`pr`.`libro_id` = `l`.`libro_id`)) WHERE (`pr`.`fecha_vencimiento` < curdate() OR `pr`.`fecha_vencimiento` = curdate()) AND `es`.`nombre` = 'pendiente''pendiente'  ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD PRIMARY KEY (`usuario_id`);

--
-- Indices de la tabla `autores`
--
ALTER TABLE `autores`
  ADD PRIMARY KEY (`autor_id`);

--
-- Indices de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  ADD PRIMARY KEY (`devolucion_id`),
  ADD KEY `prestamo_id` (`prestamo_id`),
  ADD KEY `estado_id` (`estados_id`);

--
-- Indices de la tabla `editoriales`
--
ALTER TABLE `editoriales`
  ADD PRIMARY KEY (`editorial_id`);

--
-- Indices de la tabla `estado`
--
ALTER TABLE `estado`
  ADD PRIMARY KEY (`estados_id`);

--
-- Indices de la tabla `estudiantes`
--
ALTER TABLE `estudiantes`
  ADD PRIMARY KEY (`usuario_id`);

--
-- Indices de la tabla `libros`
--
ALTER TABLE `libros`
  ADD PRIMARY KEY (`libro_id`),
  ADD KEY `autor_id` (`autor_id`),
  ADD KEY `editorial_id` (`editorial_id`);

--
-- Indices de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD PRIMARY KEY (`prestamo_id`),
  ADD KEY `libro_id` (`libro_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`usuario_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `autores`
--
ALTER TABLE `autores`
  MODIFY `autor_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT de la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  MODIFY `devolucion_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `editoriales`
--
ALTER TABLE `editoriales`
  MODIFY `editorial_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `estado`
--
ALTER TABLE `estado`
  MODIFY `estados_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `libros`
--
ALTER TABLE `libros`
  MODIFY `libro_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT de la tabla `prestamos`
--
ALTER TABLE `prestamos`
  MODIFY `prestamo_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `usuario_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=123;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `administradores`
--
ALTER TABLE `administradores`
  ADD CONSTRAINT `administradores_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`usuario_id`);

--
-- Filtros para la tabla `devoluciones`
--
ALTER TABLE `devoluciones`
  ADD CONSTRAINT `devoluciones_ibfk_1` FOREIGN KEY (`prestamo_id`) REFERENCES `prestamos` (`prestamo_id`);

--
-- Filtros para la tabla `estudiantes`
--
ALTER TABLE `estudiantes`
  ADD CONSTRAINT `estudiantes_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`usuario_id`);

--
-- Filtros para la tabla `libros`
--
ALTER TABLE `libros`
  ADD CONSTRAINT `libros_ibfk_1` FOREIGN KEY (`autor_id`) REFERENCES `autores` (`autor_id`),
  ADD CONSTRAINT `libros_ibfk_2` FOREIGN KEY (`editorial_id`) REFERENCES `editoriales` (`editorial_id`);

--
-- Filtros para la tabla `prestamos`
--
ALTER TABLE `prestamos`
  ADD CONSTRAINT `prestamos_ibfk_1` FOREIGN KEY (`libro_id`) REFERENCES `libros` (`libro_id`),
  ADD CONSTRAINT `prestamos_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`usuario_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
