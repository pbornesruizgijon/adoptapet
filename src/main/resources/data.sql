-- Insertar Roles primero (importante por las claves foráneas)
INSERT INTO roles (id, nombre) VALUES (1, 'USER'), (2, 'ADMIN');

-- Insertar Usuarios (Adopters)
INSERT INTO adopter (id, email, github_id, nombre, password, telefono) VALUES
(1, 'user@email.com', NULL, 'user', '$2a$10$zutRPSVFu1crjsyGcpZUH.RmBTuNHyiem6jOFZ8dOuKj4HGwTRIm6', '123456789'),
(2, 'admin@email.com', NULL, 'admin', '$2a$10$16tzwUhVMltYgv4pD4u/ge3thBWzUvXIcg2CU8WnqjLZFlOlHqEmm', '987654321');

-- Asignar Roles
INSERT INTO adopter_roles (adopter_id, role_id) VALUES (1, 1), (2, 2);

-- Insertar Mascotas (Cambiamos b'0' por false y b'1' por true)
INSERT INTO mascota (tipo_animal, id, adoptada, descripcion, edad, imagen, nombre, raza, sexo, esterilizado, adiestrado, adopter_id) VALUES
('PERRO', 1, false, 'Activo y juguetón', 0, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698467/file_sma2rt.webp', 'Rex', 'Pastor Alemán', 'Macho', NULL, true, NULL),
('PERRO', 2, false, 'Está loco perdido, inquieto y divertido', 1, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698344/file_ekglow.jpg', 'Archie', 'Border Collie', 'Macho', NULL, false, NULL),
('PERRO', 3, false, 'Fiel y protector', 4, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698481/file_hegcwc.webp', 'Thor', 'Bulldog Inglés', 'Macho', NULL, true, NULL),
('PERRO', 4, false, 'Muy juguetón y travieso', 0, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698384/file_dgbtrg.jpg', 'Mailo', 'Teckel', 'Macho', NULL, false, NULL),
('PERRO', 5, false, 'Tranquila', 4, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698370/file_yrwjpi.webp', 'Luna', 'Perro de agua', 'Hembra', NULL, true, NULL),
('GATO', 6, false, 'Cariñosa y juguetona', 2, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698439/file_jetis6.jpg', 'Misu', 'Europeo', 'Hembra', true, NULL, NULL),
('GATO', 7, false, 'Independiente y calmado', 5, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698456/file_oov8do.jpg', 'Nube', 'Persa', 'Macho', true, NULL, NULL),
('GATO', 8, false, 'Muy curiosa', 3, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698397/file_e32rzw.jpg', 'Mimi', 'Siames', 'Hembra', false, NULL, NULL),
('GATO', 9, false, 'Color claro y tranquila', 2, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698354/file_nrypx1.jpg', 'Lilith', 'Siames Redpoint', 'Hembra', true, NULL, NULL),
('GATO', 10, false, 'Muy dormilona', 3, 'https://res.cloudinary.com/dcvadpevd/image/upload/v1771698327/file_s6zmyu.jpg', 'Lila', 'Maine coon', 'Hembra', true, NULL, NULL);

-- Ajustar los contadores (Secuencias) para que no choquen al insertar nuevos datos
SELECT setval('adopter_id_seq', (SELECT max(id) FROM adopter));
SELECT setval('mascota_id_seq', (SELECT max(id) FROM mascota));
SELECT setval('roles_id_seq', (SELECT max(id) FROM roles));