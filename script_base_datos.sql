CREATE DATABASE IF NOT EXISTS tienda_online_db;
USE tienda_online_db;


CREATE TABLE Roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT
) ENGINE=InnoDB;

CREATE TABLE Usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    ultimo_acceso DATETIME,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
) ENGINE=InnoDB;

CREATE TABLE Direcciones (
    id_direccion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    tipo_direccion ENUM('ENVIO', 'FACTURACION', 'AMBAS') DEFAULT 'ENVIO',
    calle_numero VARCHAR(255) NOT NULL,
    colonia_barrio VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    estado_provincia VARCHAR(100) NOT NULL,
    codigo_postal VARCHAR(20) NOT NULL,
    pais VARCHAR(100) DEFAULT 'México',
    referencias TEXT,
    es_predeterminada BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Marcas (
    id_marca INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE Categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria_padre INT NULL, -- permitir subcategorías
    nombre VARCHAR(100) NOT NULL,
    slug VARCHAR(100) UNIQUE NOT NULL, --URLs amigables (ej. /ropa/camisas)
    descripcion TEXT,
    icono_url VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria_padre) REFERENCES Categorias(id_categoria) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE Productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    id_marca INT,
    nombre VARCHAR(200) NOT NULL,
    slug VARCHAR(200) UNIQUE NOT NULL,
    descripcion_corta VARCHAR(255),
    descripcion_larga TEXT,
    precio_base DECIMAL(10, 2) NOT NULL,
    peso_kg DECIMAL(8, 3),
    dimensiones VARCHAR(50), -- Ej: "20x30x15 cm"
    destacado BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_categoria) REFERENCES Categorias(id_categoria),
    FOREIGN KEY (id_marca) REFERENCES Marcas(id_marca)
) ENGINE=InnoDB;

CREATE TABLE Variantes_Producto (
    id_variante INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    sku VARCHAR(100) UNIQUE NOT NULL,
    color VARCHAR(50),
    talla_tamano VARCHAR(50),
    precio_adicional DECIMAL(10, 2) DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    imagen_especifica_url VARCHAR(255),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Imagenes_Producto (
    id_imagen INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    url_imagen VARCHAR(500) NOT NULL,
    es_principal BOOLEAN DEFAULT FALSE,
    orden INT DEFAULT 0,
    fecha_subida DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE Resenas (
    id_resena INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    calificacion INT NOT NULL CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_resena DATETIME DEFAULT CURRENT_TIMESTAMP,
    aprobada BOOLEAN DEFAULT FALSE, -- Para moderación
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE CASCADE,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
) ENGINE=InnoDB;

CREATE TABLE Listas_Deseos (
    id_lista INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_usuario, id_producto), -- Un usuario no puede agregar el mismo producto dos veces
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Cupones (
    id_cupon INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    tipo_descuento ENUM('PORCENTAJE', 'MONTO_FIJO') NOT NULL,
    valor_descuento DECIMAL(10, 2) NOT NULL,
    compra_minima DECIMAL(10, 2) DEFAULT 0.00,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    limite_uso INT DEFAULT NULL,
    veces_usado INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;


CREATE TABLE Carritos_Compras (
    id_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL, -- Un carrito activo por usuario
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Items_Carrito (
    id_item_carrito INT AUTO_INCREMENT PRIMARY KEY,
    id_carrito INT NOT NULL,
    id_variante INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_carrito) REFERENCES Carritos_Compras(id_carrito) ON DELETE CASCADE,
    FOREIGN KEY (id_variante) REFERENCES Variantes_Producto(id_variante)
) ENGINE=InnoDB;


CREATE TABLE Pedidos (
    id_pedido INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_direccion_envio INT NOT NULL,
    id_cupon INT NULL,
    numero_orden VARCHAR(50) UNIQUE NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    descuento_aplicado DECIMAL(10, 2) DEFAULT 0.00,
    costo_envio DECIMAL(10, 2) DEFAULT 0.00,
    total_final DECIMAL(10, 2) NOT NULL,
    estado_pedido ENUM('PENDIENTE', 'PROCESANDO', 'ENVIADO', 'ENTREGADO', 'CANCELADO', 'REEMBOLSADO') DEFAULT 'PENDIENTE',
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario),
    FOREIGN KEY (id_direccion_envio) REFERENCES Direcciones(id_direccion),
    FOREIGN KEY (id_cupon) REFERENCES Cupones(id_cupon)
) ENGINE=InnoDB;

CREATE TABLE Detalles_Pedido (
    id_detalle INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_variante INT NOT NULL,
    nombre_producto_snapshot VARCHAR(200) NOT NULL, -- Guardar el nombre en el momento de la compra
    cantidad INT NOT NULL,
    precio_unitario_snapshot DECIMAL(10, 2) NOT NULL, -- Guardar el precio en el momento de la compra
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_variante) REFERENCES Variantes_Producto(id_variante)
) ENGINE=InnoDB;

CREATE TABLE Pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    metodo_pago ENUM('TARJETA_CREDITO', 'TARJETA_DEBITO', 'PAYPAL', 'TRANSFERENCIA', 'OXXO') NOT NULL,
    transaccion_id VARCHAR(100), -- ID devuelto por la pasarela de pago (Stripe, PayPal, etc.)
    monto_pagado DECIMAL(10, 2) NOT NULL,
    estado_pago ENUM('PENDIENTE', 'COMPLETADO', 'FALLIDO', 'REEMBOLSADO') DEFAULT 'PENDIENTE',
    fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE Envios (
    id_envio INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT UNIQUE NOT NULL,
    paqueteria VARCHAR(100),
    numero_guia VARCHAR(100),
    url_rastreo VARCHAR(255),
    estado_envio ENUM('PREPARANDO', 'RECOLECTADO', 'EN_TRANSITO', 'ENTREGADO', 'DEVUELTO') DEFAULT 'PREPARANDO',
    fecha_estimada_entrega DATE,
    fecha_envio DATETIME,
    fecha_entrega DATETIME,
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido) ON DELETE CASCADE
) ENGINE=InnoDB;