USE `inventario_db`;

SET FOREIGN_KEY_CHECKS = 0;

-- 1. Tabla productos
DROP TABLE IF EXISTS `productos`;
CREATE TABLE `productos` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` double DEFAULT NULL,
  `stock` int NOT NULL,
  `stock_minimo` int DEFAULT '5',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `productos` VALUES 
  (2, 'Mouse Gamer', 'Mouse RGB', 120, 15, 5, '2026-05-07 07:04:50'),
  (6, 'Teclado Gamer', 'Gamer', 15, 5, 5, '2026-05-07 22:24:27'), -- Borde (Stock igual al mínimo)
  (7, 'Pantalla Gamer', 'Pantalla', 500, 0, 5, '2026-05-11 04:17:03'), -- Límite (Agotado)
  (8, 'CPU', 'CPU', 5000, -2, 2, '2026-05-11 04:19:48'), -- Caso negativo (Stock imposible)
  (9, 'Audifonos', 'audifonos', 15, 50, 10, '2026-05-11 04:32:12'),
  (10, 'Microfono', 'microfono', 20, 60, 15, '2026-05-11 04:37:46'),
  (11, 'Tarjeta ram', 'Tar', 400, 15, 5, '2026-05-11 04:53:14'),
  (12, 'almacenamiento', 'almacenamiento', 160, 50, 13, '2026-05-11 04:57:36'),
  (13, 'periféricos externos', 'periféricos externos', 60, 39, 19, '2026-05-11 05:05:18'),
  (14, 'placa base', 'placa base', 600, 40, 10, '2026-05-11 05:09:12'),
  (15, 'SSD', 'SSD', 50, 20, 5, '2026-05-11 05:14:39');

-- 2. Tabla movimiento (Normalizada con Foreign Key)
DROP TABLE IF EXISTS `movimiento`;
CREATE TABLE `movimiento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cantidad` int NOT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `producto_id` bigint DEFAULT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_movimiento_producto` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Mapeo de los nombres a los IDs correspondientes de la tabla productos
INSERT INTO `movimiento` VALUES 
  (1, 10, '2026-05-10 23:17:03.499664', 7, 'ENTRADA'),
  (2, 5, '2026-05-10 23:19:48.430673', 8, 'ENTRADA'),
  (3, 50, '2026-05-10 23:32:12.913320', 9, 'ENTRADA'),
  (4, 60, '2026-05-10 23:37:46.577294', 10, 'ENTRADA'),
  (5, 15, '2026-05-10 23:53:14.133630', 11, 'ENTRADA'),
  (6, 50, '2026-05-10 23:57:36.654936', 12, 'ENTRADA'),
  (7, 39, '2026-05-11 00:05:18.124837', 13, 'ENTRADA'),
  (8, 40, '2026-05-11 00:09:12.915768', 14, 'ENTRADA'),
  (9, 20, '2026-05-11 00:14:39.899985', 15, 'ENTRADA');

-- 3. Tabla usuarios
DROP TABLE IF EXISTS `usuarios`;
CREATE TABLE `usuarios` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `rol` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `usuarios` VALUES 
  (1, '1234', 'admin', 'ADMIN'),
  (2, '1234', 'empleado', 'EMPLEADO'),
  (3, '1234', 'visitante', 'INVITADO'),
  (4, '1234', 'suspendido', 'INACTIVO');

SET FOREIGN_KEY_CHECKS = 1;