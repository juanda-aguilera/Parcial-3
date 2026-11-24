-- =========================================================
-- Base de datos del sistema de pedidos de la cafetería
-- Ejecutar en phpMyAdmin (XAMPP) o en la consola de MySQL
-- =========================================================
SET NAMES utf8mb4;
DROP DATABASE IF EXISTS cafeteria_patrones;
CREATE DATABASE cafeteria_patrones CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE cafeteria_patrones;

-- Menú configurable (reemplaza a menu.txt cuando hay conexión)
CREATE TABLE producto_menu (
    codigo     VARCHAR(10)  NOT NULL,
    nombre     VARCHAR(80)  NOT NULL,
    categoria  ENUM('BEBIDA','PLATO','POSTRE') NOT NULL,
    precio     DECIMAL(10,2) NOT NULL,
    atributo   VARCHAR(20)  NULL,
    activo     TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (codigo),
    CONSTRAINT chk_precio_menu CHECK (precio > 0)
) ENGINE=InnoDB;

-- Cada cierre de caja (turno)
CREATE TABLE cierre_caja (
    id                INT AUTO_INCREMENT NOT NULL,
    fecha             DATETIME      NOT NULL,
    cantidad_ventas   INT           NOT NULL,
    total_bruto       DECIMAL(12,2) NOT NULL,
    total_descuentos  DECIMAL(12,2) NOT NULL,
    total_neto        DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Pedidos y su estado actual (patrón State)
CREATE TABLE pedido (
    id                   INT          NOT NULL,
    mesa                 VARCHAR(40)  NOT NULL,
    estado               ENUM('REGISTRADO','EN_PREPARACION','LISTO','ENTREGADO','PAGADO','CANCELADO') NOT NULL,
    fecha_creacion       DATETIME     NOT NULL,
    fecha_actualizacion  DATETIME     NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Productos del pedido tal como se vendieron (con adiciones y combos ya resueltos)
CREATE TABLE detalle_pedido (
    id           INT AUTO_INCREMENT NOT NULL,
    pedido_id    INT           NOT NULL,
    posicion     INT           NOT NULL,
    nombre       VARCHAR(80)   NOT NULL,
    descripcion  TEXT          NOT NULL,
    categoria    ENUM('BEBIDA','PLATO','POSTRE','COMBO') NOT NULL,
    precio       DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_detalle_posicion (pedido_id, posicion),
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Ventas pagadas; cierre_id queda NULL hasta el cierre de caja
CREATE TABLE venta (
    id           INT AUTO_INCREMENT NOT NULL,
    pedido_id    INT           NOT NULL,
    subtotal     DECIMAL(12,2) NOT NULL,
    descuento    DECIMAL(12,2) NOT NULL,
    total        DECIMAL(12,2) NOT NULL,
    metodo_pago  ENUM('EFECTIVO','TARJETA','BILLETERA') NOT NULL,
    promocion    VARCHAR(80)   NOT NULL,
    fecha        DATETIME      NOT NULL,
    cierre_id    INT           NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_venta_pedido (pedido_id),
    CONSTRAINT fk_venta_pedido FOREIGN KEY (pedido_id) REFERENCES pedido (id),
    CONSTRAINT fk_venta_cierre FOREIGN KEY (cierre_id) REFERENCES cierre_caja (id)
) ENGINE=InnoDB;

-- Datos iniciales del menú
INSERT INTO producto_menu (codigo, nombre, categoria, precio, atributo) VALUES
('B01', 'Café americano',          'BEBIDA', 4500,  'caliente'),
('B02', 'Capuchino',               'BEBIDA', 7000,  'caliente'),
('B03', 'Té chai',                 'BEBIDA', 6500,  'caliente'),
('B04', 'Jugo de mora',            'BEBIDA', 6000,  'fria'),
('B05', 'Frappé de café',          'BEBIDA', 9500,  'fria'),
('P01', 'Sándwich de pollo',       'PLATO',  16000, NULL),
('P02', 'Arepa rellena',           'PLATO',  12000, NULL),
('P03', 'Ensalada César',          'PLATO',  18000, NULL),
('P04', 'Hamburguesa clásica',     'PLATO',  24000, NULL),
('R01', 'Brownie',                 'POSTRE', 6000,  NULL),
('R02', 'Cheesecake de maracuyá',  'POSTRE', 9000,  NULL),
('R03', 'Torta de zanahoria',      'POSTRE', 8000,  NULL);

-- Vista de apoyo para el reporte de ventas
CREATE VIEW vista_ventas AS
SELECT v.id, v.pedido_id, p.mesa, v.subtotal, v.descuento, v.total,
       v.metodo_pago, v.promocion, v.fecha, v.cierre_id
FROM venta v
JOIN pedido p ON p.id = v.pedido_id;
