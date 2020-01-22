-- phpMyAdmin SQL Dump
-- version 4.9.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 08-11-2019 a las 10:48:40
-- Versión del servidor: 10.4.8-MariaDB
-- Versión de PHP: 7.1.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `di_t02_p02_e1`
--
CREATE DATABASE IF NOT EXISTS `di_t02_p02_e1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `di_t02_p02_e1`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` smallint(5) UNSIGNED NOT NULL,
  `dni` varchar(9) COLLATE utf8mb4_spanish_ci NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8mb4_spanish_ci NOT NULL,
  `direccion` varchar(150) COLLATE utf8mb4_spanish_ci DEFAULT NULL,
  `telefono` int(9) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `dni`, `nombre`, `apellidos`, `direccion`, `telefono`) VALUES
(1, '11111111A', 'Rey', 'Desconocidos', 'Jakku', 611111111),
(2, '22222222B', 'Han', 'Solo', 'Corellia', 622222222),
(3, '33333333C', 'Leia', 'Organa', 'Alderaan', 633333333),
(4, '44444444D', 'Kylo', 'Ren', 'Desconocida', 644444444),
(5, '55555555E', 'Legolas', 'Thranduil', 'Bosque Negro', 655555555),
(6, '66666666F', 'Bilbo', 'Bolson', 'Hobbiton', 666666666),
(7, '77777777G', 'Kíli', 'Durin', 'Ered Luin', 677777777),
(8, '88888888H', 'Gandalf', 'Desconocidos', 'Desconocida', 688888888),
(9, '99999999I', 'Balin', 'Fundin', 'Moria', 699999999),
(10, '10000000J', 'Elrond', 'Eärendil', 'Rivendel', 600000000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `eventos`
--

CREATE TABLE `eventos` (
  `id` smallint(5) UNSIGNED NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `tipo` enum('Banquete','Jornada','Congreso') NOT NULL,
  `asistentes` smallint(5) UNSIGNED NOT NULL,
  `num_dias` smallint(5) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `eventos`
--

INSERT INTO `eventos` (`id`, `nombre`, `tipo`, `asistentes`, `num_dias`) VALUES
(1, 'Congreso internacional sobre la historia de Kashyyyk', 'Congreso', 75, 3),
(2, 'Cumpleaños de Han Solo', 'Banquete', 175, 1),
(3, 'Kylo Ren, la fuerza perdida', 'Jornada', 300, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `habitacion_congresos`
--

CREATE TABLE `habitacion_congresos` (
  `id_evento` smallint(5) UNSIGNED NOT NULL,
  `num_hab` smallint(5) UNSIGNED NOT NULL,
  `tipo` enum('individual','doble') COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci COMMENT='Con JAVA se controla que solo haya almacenados aquí eventos de tipo CONGRESO.';

--
-- Volcado de datos para la tabla `habitacion_congresos`
--

INSERT INTO `habitacion_congresos` (`id_evento`, `num_hab`, `tipo`) VALUES
(1, 38, 'doble');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `menu_banquetes`
--

CREATE TABLE `menu_banquetes` (
  `id_evento` smallint(5) UNSIGNED NOT NULL,
  `primero` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `segundo` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci COMMENT='Con JAVA se controla que solo haya almacenados aquí eventos de tipo BANQUETE.';

--
-- Volcado de datos para la tabla `menu_banquetes`
--

INSERT INTO `menu_banquetes` (`id_evento`, `primero`, `segundo`) VALUES
(2, 'croquetas', 'solomillo');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `reservas`
--

CREATE TABLE `reservas` (
  `id_cliente` smallint(5) UNSIGNED NOT NULL,
  `id_salon` smallint(5) UNSIGNED NOT NULL,
  `id_evento` smallint(5) UNSIGNED NOT NULL,
  `fecha` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Esta tabla no es necesaria y toda esta información se puede almacenar en la tabla eventos. Sin embargo, se ha decidido crear para simplificar las consultas de disponibilidad.';

--
-- Volcado de datos para la tabla `reservas`
--

INSERT INTO `reservas` (`id_cliente`, `id_salon`, `id_evento`, `fecha`) VALUES
(3, 1, 1, '2019-12-06'),
(3, 1, 1, '2019-12-07'),
(3, 1, 1, '2019-12-08'),
(4, 3, 2, '2019-11-30'),
(9, 4, 3, '2019-12-21');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `salones`
--

CREATE TABLE `salones` (
  `id` smallint(5) UNSIGNED NOT NULL,
  `nombre` varchar(50) COLLATE utf8mb4_spanish_ci NOT NULL,
  `capacidad` smallint(5) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

--
-- Volcado de datos para la tabla `salones`
--

INSERT INTO `salones` (`id`, `nombre`, `capacidad`) VALUES
(1, 'Tierra Media', 100),
(2, 'Narnia', 150),
(3, 'Asgard', 250),
(4, 'Namek', 500);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `DNI_UNIQUE` (`dni`);

--
-- Indices de la tabla `eventos`
--
ALTER TABLE `eventos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `habitacion_congresos`
--
ALTER TABLE `habitacion_congresos`
  ADD PRIMARY KEY (`id_evento`);

--
-- Indices de la tabla `menu_banquetes`
--
ALTER TABLE `menu_banquetes`
  ADD PRIMARY KEY (`id_evento`);

--
-- Indices de la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD PRIMARY KEY (`id_salon`,`fecha`),
  ADD KEY `CLI_FK` (`id_cliente`),
  ADD KEY `EVE_FK` (`id_evento`);

--
-- Indices de la tabla `salones`
--
ALTER TABLE `salones`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `nombre` (`nombre`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `eventos`
--
ALTER TABLE `eventos`
  MODIFY `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `salones`
--
ALTER TABLE `salones`
  MODIFY `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `habitacion_congresos`
--
ALTER TABLE `habitacion_congresos`
  ADD CONSTRAINT `CONG_FK` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `menu_banquetes`
--
ALTER TABLE `menu_banquetes`
  ADD CONSTRAINT `BANQ_FK` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `reservas`
--
ALTER TABLE `reservas`
  ADD CONSTRAINT `CLI_FK` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`),
  ADD CONSTRAINT `EVE_FK` FOREIGN KEY (`id_evento`) REFERENCES `eventos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `SAL_FK` FOREIGN KEY (`id_salon`) REFERENCES `salones` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
