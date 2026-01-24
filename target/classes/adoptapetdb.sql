-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: mysql
-- Tiempo de generación: 24-01-2026 a las 11:39:20
-- Versión del servidor: 8.0.45
-- Versión de PHP: 8.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `adoptapetdb`
--
CREATE DATABASE IF NOT EXISTS `adoptapetdb` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `adoptapetdb`;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adopter`
--

CREATE TABLE `adopter` (
  `id` bigint NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `adopter`
--

INSERT INTO `adopter` (`id`, `email`, `nombre`, `password`, `telefono`) VALUES
(1, 'user@email.com', 'user', '$2a$10$Gon1Vm1p8vYEoWErf6VpTe/3Cke6xstGjXbbteXNoGQALGPEVPy1q', '123456789'),
(2, 'admin@email.com', 'admin', '$2a$10$kNf0BavCT22Ouv/eZGUGZ.Vg8GFudoLtJweg.78gQbnhUYCvQmCb6', '987654321');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `adopter_roles`
--

CREATE TABLE `adopter_roles` (
  `adopter_id` bigint NOT NULL,
  `role_id` bigint NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `adopter_roles`
--

INSERT INTO `adopter_roles` (`adopter_id`, `role_id`) VALUES
(1, 1),
(2, 1),
(2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mascota`
--

CREATE TABLE `mascota` (
  `id` bigint NOT NULL,
  `adoptada` bit(1) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `edad` int NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `raza` varchar(255) DEFAULT NULL,
  `sexo` varchar(255) DEFAULT NULL,
  `adopter_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `mascota`
--

INSERT INTO `mascota` (`id`, `adoptada`, `descripcion`, `edad`, `imagen`, `nombre`, `raza`, `sexo`, `adopter_id`) VALUES
(1, b'0', 'Activo y juguetón', 0, '1_pastor_aleman.jpg', 'Rex', 'Pastor Alemán', 'Macho', NULL),
(2, b'0', 'Cariñosa y juguetona', 2, '2_gato_europeo.jpg', 'Misu', 'Europeo', 'Hembra', NULL),
(3, b'0', 'Está loco perdido, inquieto y divertido', 1, '3_border_collie.jpg', 'Archie', 'Border Collie', 'Macho', NULL),
(4, b'0', 'Independiente y calmado', 5, '4_persa.jpg', 'Nube', 'Persa', 'Macho', NULL),
(5, b'0', 'Fiel y protector', 4, '5_bulldog_ingles.jpg', 'Thor', 'Bulldog Inglés', 'Macho', NULL),
(6, b'0', 'Muy curiosa', 3, '6_siames.jpg', 'Mimi', 'Siames', 'Hembra', NULL),
(7, b'0', 'Muy juguetón y travieso', 0, '7_teckel.jpg', 'Mailo', 'Teckel', 'Macho', NULL),
(8, b'0', 'Color claro y tranquila', 2, '8_siames_redpoint.jpg', 'Lilith', 'Siames Redpoint', 'Hembra', NULL),
(9, b'0', 'Tranquila', 4, '9_perro_agua.jpg', 'Luna', 'Perro de agua', 'Hembra', NULL),
(10, b'1', 'Muy dormilona', 3, '10_maine_coon.jpg', 'Lila', 'Maine coon', 'Hembra', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(2, 'ADMIN'),
(1, 'USER');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `adopter`
--
ALTER TABLE `adopter`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `adopter_roles`
--
ALTER TABLE `adopter_roles`
  ADD PRIMARY KEY (`adopter_id`,`role_id`),
  ADD KEY `FKlimbj3r6qrjf4nsj6dwrma918` (`role_id`);

--
-- Indices de la tabla `mascota`
--
ALTER TABLE `mascota`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKi7xlt16l9r3tbj3q3bugpg1kg` (`adopter_id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKldv0v52e0udsh2h1rs0r0gw1n` (`nombre`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `adopter`
--
ALTER TABLE `adopter`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `mascota`
--
ALTER TABLE `mascota`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `adopter_roles`
--
ALTER TABLE `adopter_roles`
  ADD CONSTRAINT `FKlimbj3r6qrjf4nsj6dwrma918` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKqrwy2kjl8qjn486afnj2ah0en` FOREIGN KEY (`adopter_id`) REFERENCES `adopter` (`id`);

--
-- Filtros para la tabla `mascota`
--
ALTER TABLE `mascota`
  ADD CONSTRAINT `FKi7xlt16l9r3tbj3q3bugpg1kg` FOREIGN KEY (`adopter_id`) REFERENCES `adopter` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
