# Lista de Deseos - Carvajal

Este proyecto es una prueba tecnica para Carvajal donde se implementa una aplicacion web de **Lista de Deseos** para un sistema de e-commerce. El usuario puede ver el catalogo de productos, agregar productos a su lista de deseos, actualizar la cantidad o eliminarlos, y consultar el historico de todas las acciones que ha realizado en su lista.

La aplicacion tambien notifica al usuario cuando alguno de los productos de su lista ya no tiene stock disponible.

## Tecnologias utilizadas

Para este proyecto utilice las siguientes herramientas y tecnologias:

### Backend
- **Java 17**
- **Spring Boot 3.x** - framework para construir el API REST
- **Spring Data JPA / Hibernate** - como ORM para la persistencia
- **Lombok** - para reducir codigo repetitivo (getters, setters, constructores)
- **Maven** - gestor de dependencias del backend
- **MySQL Connector/J** - driver para conectarse a MySQL

### Frontend
- **Angular 17** (con NgModules)
- **TypeScript**
- **HTML5 y CSS3** (estilos propios, sin frameworks)
- **RxJS** - para el manejo de Observables en las llamadas al backend

### Base de datos
- **MySQL 8**

## ¿Por que elegi MySQL?

Decidi usar MySQL como motor de base de datos por las siguientes razones:

1. **Los datos del proyecto tienen relaciones claras**: un usuario tiene una lista de deseos, una lista tiene productos asociados, y existen registros historicos que dependen de usuarios y productos. Una base de datos **relacional** es la mejor opcion para representar estas relaciones con llaves foraneas.
2. **Es una base de datos ampliamente usada** en proyectos empresariales, gratuita, y muy bien soportada por Spring Data JPA.
3. **Facil de configurar localmente** con XAMPP, WAMP o Docker, lo que facilita el despliegue para quien revise la prueba.
4. **Las consultas que necesita el proyecto son sencillas** (buscar por usuario, filtrar por producto, ordenar por fecha), no requieren un motor NoSQL.

## Estructura del proyecto

El proyecto esta dividido en dos carpetas principales:

WISHLISTT/
├── wishlist/             # Backend en Spring Boot
│   ├── src/main/java/com/carvajal/wishlist/
│   │   ├── controller/   # Endpoints REST
│   │   ├── dto/          # Objetos de transferencia
│   │   ├── entity/       # Entidades JPA
│   │   ├── repository/   # Repositorios Spring Data
│   │   ├── service/      # Logica de negocio
│   │   └── WishlistApplication.java
│   ├── src/main/resources/
│   │   └── application.yaml
│   └── pom.xml
│
└── WishlistFrontend/     # Frontend en Angular
    └── src/app/
        ├── core/         # Modulo nucleo (HttpClient)
        ├── features/
        │   ├── catalogo/ # Modulo de catalogo
        │   └── deseos/   # Modulo de lista y historico
        ├── app.component.*
        ├── app.module.ts
        └── app-routing.module.ts


## Modelo de base de datos

La base de datos `wishlist_db` cuenta con 4 tablas:

- **usuarios** - almacena la informacion del cliente
- **productos** - catalogo de productos que ofrece Carvajal
- **lista_deseos** - items que el usuario tiene en su lista de deseos
- **historico_lista_deseos** - registro de todas las acciones (agregar, actualizar, eliminar)

### Relaciones

- Un **usuario** puede tener muchos items en su lista de deseos (1 a N)
- Un **producto** puede estar en muchas listas de deseos (1 a N)
- Un **usuario** tiene muchos registros en el historico (1 a N)
- Un **producto** puede aparecer en muchos registros del historico (1 a N)

### Diagrama Entidad-Relacion
 La imagen del modelo se encuentra en la carpeta `docs/` del repositorio.


## Manual de despliegue

### Pre-requisitos

Antes de levantar el proyecto, asegurate de tener instalado lo siguiente:

| Herramienta | Version recomendada |
| --- | --- |
| Java JDK | 17 o superior |
| Maven | 3.8 o superior (o usar el wrapper `mvnw` incluido) |
| Node.js | 20 o superior |
| Angular CLI | 17 |
| MySQL | 8.x |

Para instalar Angular CLI:

```bash
npm install -g @angular/cli
```


### Paso 1: Crear la base de datos

Abre tu cliente de MySQL (MySQL Workbench, DBeaver, consola, etc.) y ejecuta el siguiente script. Este script crea la base de datos, las tablas, las relaciones y carga un usuario de prueba mas un catalogo de productos inicial:

```sql
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

-- Usuario de prueba
INSERT INTO usuarios (nombre, email) VALUES
('Juan Perez', 'juan.perez@carvajal.com');

-- Catalogo de productos inicial
INSERT INTO productos (nombre, descripcion, precio, stock, categoria) VALUES
('Cuaderno Universitario 100 hojas', 'Cuaderno cuadriculado tapa dura',           12500.00,  50, 'Papeleria'),
('Esferos Kilometricos x12',         'Caja con 12 esferos negros',                18000.00,  30, 'Papeleria'),
('Calculadora Cientifica',           'Calculadora cientifica con 240 funciones',  89000.00,  15, 'Tecnologia'),
('Resma Papel Carta',                'Resma de 500 hojas papel carta 75 gr',      22000.00, 100, 'Papeleria'),
('Agenda Ejecutiva 2026',            'Agenda con planificador anual y mensual',   35000.00,  25, 'Oficina'),
('Marcadores Borrables x6',          'Set de marcadores para tablero',            24500.00,   0, 'Papeleria'),
('Carpeta Legajadora Plastica',      'Carpeta tamano oficio con gancho',           5500.00, 200, 'Oficina'),
('Libro - El Principito',            'Edicion ilustrada tapa dura',               32000.00,   8, 'Libros'),
('Mochila Escolar',                  'Mochila resistente con compartimientos',    78000.00,   0, 'Accesorios'),
('Pegante en Barra',                 'Pegante en barra 40gr',                      4500.00, 150, 'Papeleria');


Cargue varios productos con stock en 0 (Marcadores Borrables, Mochila Escolar) a proposito, para poder probar facilmente la notificacion de "sin stock" cuando estan en la lista de deseos.


### Paso 2: Configurar las credenciales del backend

El archivo `wishlist/src/main/resources/application.yaml` contiene la configuracion de conexion a la base de datos. Ajusta el usuario y contrasena segun tu instalacion de MySQL:

```yaml
spring:
  application:
    name: wishlist

  datasource:
    url: jdbc:mysql://localhost:3306/wishlist_db
    username: root
    password:
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

server:
  port: 8080
```

**Credenciales por defecto:**
- Usuario: `root`
- Contrasena: *(vacia)*
- Base de datos: `wishlist_db`
- Puerto: `3306`

Si tu MySQL tiene contrasena, ponla en la propiedad `password`.

### Paso 3: Levantar el backend

Abre una terminal en la carpeta `wishlist/` y ejecuta:

mvn spring-boot:run

El backend quedara corriendo en `http://localhost:8080`.

Para verificar que esta arriba, puedes abrir en el navegador:

http://localhost:8080/productos/catalogo

Y deberias ver un JSON con los productos.


### Paso 4: Levantar el frontend

Abre **otra** terminal en la carpeta `WishlistFrontend/` y ejecuta:

# Levantar el servidor de desarrollo
ng serve


El frontend quedara corriendo en `http://localhost:4200`. Abre esa URL en el navegador y veras la aplicacion.

### Paso 5: Probar la aplicacion

Una vez levantadas las dos partes, en el navegador:

1. **Catalogo** (`/catalogo`) - veras el listado de productos. Los que tienen stock en 0 muestran el boton "Agregar" deshabilitado.
2. **Mi Lista** (`/deseos`) - los productos que vas agregando aparecen aqui. Si algun producto se queda sin stock, veras una alerta amarilla.
3. **Historico** (`/historico`) - tabla con todas las acciones (agregar, actualizar, eliminar) realizadas por el usuario.


## Endpoints del API

### Productos

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/productos/catalogo` | Obtiene el catalogo completo de productos |

### Lista de Deseos

| Metodo | Ruta | Descripcion |
| --- | --- | --- |
| GET | `/lista-deseos/{idUsuario}` | Lista los items de la lista del usuario |
| POST | `/lista-deseos/agregar` | Agrega un producto a la lista |
| PUT | `/lista-deseos/actualizar/{idListaDeseos}` | Actualiza la cantidad de un item |
| DELETE | `/lista-deseos/eliminar/{idListaDeseos}` | Elimina un item de la lista |
| GET | `/lista-deseos/historico/{idUsuario}` | Obtiene el historico del usuario |

### Ejemplos de request body

**POST `/lista-deseos/agregar`**
```json
{
  "idUsuario": 1,
  "idProducto": 3,
  "cantidad": 2
}
```

**PUT `/lista-deseos/actualizar/{idListaDeseos}`**
```json
{
  "cantidad": 5
}
```

---

## Notas y comentarios adicionales

- El proyecto asume que el **usuario ya esta autenticado**, tal como lo indica la prueba. Por eso en el frontend el `idUsuario` esta fijo en `1` (el usuario "Juan Perez" que se inserta con el script SQL). En un proyecto real esto vendria de un servicio de autenticacion (JWT, sesion, etc.).
- Configure **CORS** en el backend para permitir que Angular (`localhost:4200`) pueda llamar al API (`localhost:8080`).
- El **stock** del producto NO se modifica cuando se agrega a la lista de deseos. Esto es intencional: la lista es solo una intencion de compra, no una compra real. El stock cambia por otros motivos (por ejemplo, otros usuarios que si compran ese producto), y por eso al consultar la lista mostramos la alerta de "ya no se encuentra en stock".
- La accion **Actualizar** del lado del cliente solo cambia la cantidad deseada del item. No modifica nada del producto en si.
- El **historico** guarda un registro por cada accion (`AGREGADO`, `ACTUALIZADO`, `ELIMINADO`), incluso cuando se elimina el item de la lista de deseos. De esa forma queda la trazabilidad completa de lo que paso.


