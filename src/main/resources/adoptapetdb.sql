-- phpMyAdmin SQL Dump
-- version 5.2.3
-- https://www.phpmyadmin.net/
--
-- Servidor: mysql
-- Tiempo de generación: 22-02-2026 a las 10:00:41
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
  `github_id` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `adopter`
--

INSERT INTO `adopter` (`id`, `email`, `github_id`, `nombre`, `password`, `telefono`) VALUES
(1, 'user@email.com', NULL, 'user', '$2a$10$zutRPSVFu1crjsyGcpZUH.RmBTuNHyiem6jOFZ8dOuKj4HGwTRIm6', '123456789'),
(2, 'admin@email.com', NULL, 'admin', '$2a$10$16tzwUhVMltYgv4pD4u/ge3thBWzUvXIcg2CU8WnqjLZFlOlHqEmm', '987654321');

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
(2, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mascota`
--

CREATE TABLE `mascota` (
  `tipo_animal` varchar(31) NOT NULL,
  `id` bigint NOT NULL,
  `adoptada` bit(1) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `edad` int NOT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `nombre` varchar(255) DEFAULT NULL,
  `raza` varchar(255) DEFAULT NULL,
  `sexo` varchar(255) DEFAULT NULL,
  `esterilizado` bit(1) DEFAULT NULL,
  `adiestrado` bit(1) DEFAULT NULL,
  `adopter_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Volcado de datos para la tabla `mascota`
--

INSERT INTO `mascota` (`tipo_animal`, `id`, `adoptada`, `descripcion`, `edad`, `imagen`, `nombre`, `raza`, `sexo`, `esterilizado`, `adiestrado`, `adopter_id`) VALUES
('PERRO', 1, b'0', 'Activo y juguetón', 0, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698467/file_sma2rt.webp', 'Rex', 'Pastor Alemán', 'Macho', NULL, b'1', NULL),
('PERRO', 2, b'0', 'Está loco perdido, inquieto y divertido', 1, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698344/file_ekglow.jpg', 'Archie', 'Border Collie', 'Macho', NULL, b'0', NULL),
('PERRO', 3, b'0', 'Fiel y protector', 4, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698481/file_hegcwc.webp', 'Thor', 'Bulldog Inglés', 'Macho', NULL, b'1', NULL),
('PERRO', 4, b'0', 'Muy juguetón y travieso', 0, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698384/file_dgbtrg.jpg', 'Mailo', 'Teckel', 'Macho', NULL, b'0', NULL),
('PERRO', 5, b'0', 'Tranquila', 4, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698370/file_yrwjpi.webp', 'Luna', 'Perro de agua', 'Hembra', NULL, b'1', NULL),
('GATO', 6, b'0', 'Cariñosa y juguetona', 2, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698439/file_jetis6.jpg', 'Misu', 'Europeo', 'Hembra', b'1', NULL, NULL),
('GATO', 7, b'0', 'Independiente y calmado', 5, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698456/file_oov8do.jpg', 'Nube', 'Persa', 'Macho', b'1', NULL, NULL),
('GATO', 8, b'0', 'Muy curiosa', 3, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698397/file_e32rzw.jpg', 'Mimi', 'Siames', 'Hembra', b'0', NULL, NULL),
('GATO', 9, b'0', 'Color claro y tranquila', 2, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698354/file_nrypx1.jpg', 'Lilith', 'Siames Redpoint', 'Hembra', b'1', NULL, NULL),
('GATO', 10, b'0', 'Muy dormilona', 3, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698327/file_s6zmyu.jpg', 'Lila', 'Maine coon', 'Hembra', b'1', NULL, NULL);

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
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK8kipob62jyuhivo8ic9qc6a2f` (`github_id`);

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
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `mascota`
--
ALTER TABLE `mascota`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

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
