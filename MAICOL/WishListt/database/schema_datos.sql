CREATE DATABASE wishlist_db;

USE wishlist_db;

CREATE TABLE usuarios (
    id_usuario       BIGINT       NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL,
    fecha_creacion   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_usuario)
);

CREATE TABLE productos (
    id_producto      BIGINT         NOT NULL AUTO_INCREMENT,
    nombre           VARCHAR(150)   NOT NULL,
    descripcion      VARCHAR(500),
    precio           DECIMAL(10, 2) NOT NULL,
    stock            INT            NOT NULL DEFAULT 0,
    categoria        VARCHAR(80),
    fecha_creacion   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_producto)
);

CREATE TABLE lista_deseos (
    id_lista_deseos  BIGINT   NOT NULL AUTO_INCREMENT,
    id_usuario       BIGINT   NOT NULL,
    id_producto      BIGINT   NOT NULL,
    cantidad         INT      NOT NULL DEFAULT 1,
    fecha_agregado   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_lista_deseos),
    FOREIGN KEY (id_usuario)  REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

CREATE TABLE historico_lista_deseos (
    id_historico   BIGINT      NOT NULL AUTO_INCREMENT,
    id_usuario     BIGINT      NOT NULL,
    id_producto    BIGINT      NOT NULL,
    cantidad       INT         NOT NULL,
    accion         VARCHAR(20) NOT NULL,
    fecha_accion   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_historico),
    FOREIGN KEY (id_usuario)  REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

INSERT INTO usuarios (nombre, email) VALUES
('Juan Perez', 'juan.perez@carvajal.com');

INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES
('Cuaderno Universitario 100 hojas', 'Cuaderno cuadriculado tapa dura', 12500.00, 50, 'Papeleria'),
('Esferos Kilometricos x12', 'Caja con 12 esferos negros', 18000.00, 30, 'Papeleria'),
('Calculadora Cientifica', 'Calculadora cientifica con 240 funciones', 89000.00, 15, 'Tecnologia'),
('Resma Papel Carta', 'Resma de 500 hojas papel carta 75 gr', 22000.00, 100, 'Papeleria'),
('Agenda Ejecutiva 2026', 'Agenda con planificador anual y mensual', 35000.00, 25, 'Oficina'),
('Marcadores Borrables x6', 'Set de marcadores para tablero', 24500.00, 0, 'Papeleria'),
('Carpeta Legajadora Plastica', 'Carpeta tamano oficio con gancho', 5500.00, 200, 'Oficina'),
('Libro - El Principito', 'Edicion ilustrada tapa dura', 32000.00, 8, 'Libros'),
('Mochila Escolar', 'Mochila resistente con compartimientos', 78000.00, 0, 'Accesorios'),
('Pegante en Barra', 'Pegante en barra 40gr', 4500.00, 150, 'Papeleria');