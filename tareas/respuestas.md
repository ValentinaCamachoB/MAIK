# API de Tareas - Prueba Técnica

API REST para gestionar tareas hecha con Spring Boot y MySQL.

## ¿Qué hace esta API?

Permite crear, leer, actualizar y eliminar tareas. Cada tarea tiene un estado (PENDIENTE, EN_PROGRESO, COMPLETADA, CANCELADA) y una prioridad (BAJA, MEDIA, ALTA, URGENTE).

## Tecnologías

- Java 17
- Spring Boot 3
- Spring Data JPA
- MySQL 8
- Maven
- Lombok

## Lo que necesitas tener instalado

- Java 17 o superior
- Maven
- MySQL 8

## Cómo ejecutar el proyecto

### 1. Crear la base de datos

Abre MySQL Workbench y ejecuta el archivo `database.sql` que está en la raíz del proyecto. Eso crea la base de datos `tareas_db` y mete 5 tareas de prueba.

### 2. Configurar la contraseña de MySQL

Abre el archivo `src/main/resources/application.yaml` y pon tu contraseña de MySQL:

```yaml
spring:
  datasource:
    password: TU_PASSWORD_AQUI
```

Si tu MySQL no tiene contraseña, déjalo vacío.

### 3. Ejecutar la aplicación

Desde la carpeta del proyecto, abre una terminal y ejecuta:

```bash
mvn spring-boot:run
```

Cuando salga el mensaje `Started TareasApplication`, la API está corriendo en `http://localhost:8080`.

## Endpoints

| Método | URL | Qué hace |
|---|---|---|
| GET | /api/tareas | Lista todas las tareas |
| GET | /api/tareas/{id} | Trae una tarea por ID |
| POST | /api/tareas | Crea una tarea nueva |
| PUT | /api/tareas/{id} | Actualiza una tarea completa |
| PATCH | /api/tareas/{id}/estado | Cambia solo el estado |
| DELETE | /api/tareas/{id} | Elimina una tarea |
| GET | /api/tareas/filtrar/estado?estado=PENDIENTE | Filtra por estado |
| GET | /api/tareas/filtrar/prioridad?prioridad=ALTA | Filtra por prioridad |
| GET | /api/tareas/buscar?q=texto | Busca por título |

### Nivel 2 - Opción C (Ordenamiento)

El endpoint `GET /api/tareas` acepta el parámetro `ordenarPor` con estos valores:

- `fechaCreacion`
- `prioridad`
- `titulo`

Ejemplo: `GET /api/tareas?ordenarPor=titulo`

## Cómo probar en Postman

### Crear una tarea (POST)

URL: `http://localhost:8080/api/tareas`

Body (raw - JSON):

```json
{
  "titulo": "Mi nueva tarea",
  "descripcion": "Algo importante",
  "prioridad": "ALTA"
}
```

### Cambiar el estado (PATCH)

URL: `http://localhost:8080/api/tareas/1/estado`

Body:

```json
{
  "estado": "COMPLETADA"
}
```

En la raíz del proyecto está el archivo `Tareas-API.postman_collection.json` que puedes importar en Postman para tener todos los endpoints listos.

## Formato de las respuestas

Todas las respuestas vienen así:

```json
{
  "success": true,
  "mensaje": "Tarea creada exitosamente",
  "data": { ... }
}
```

Si hay error, `success` es `false`.

## Códigos HTTP

| Código | Cuándo |
|---|---|
| 200 | Consulta o actualización OK |
| 201 | Tarea creada OK |
| 400 | Datos mal enviados |
| 404 | Tarea no encontrada |
| 409 | Cambio de estado no permitido |
| 500 | Error inesperado |

## Estructura del proyecto

```
src/main/java/com/example/tareas/
├── controller/   → endpoints REST
├── service/      → lógica de negocio
├── repository/   → acceso a la base de datos
├── model/        → entidad Tarea y los enums
├── dto/          → objetos para enviar/recibir datos
└── exception/    → manejo de errores
```

## Reglas de negocio

- El título debe tener al menos 3 caracteres.
- Una tarea CANCELADA no puede cambiar de estado.
- Una tarea COMPLETADA no puede volver a PENDIENTE ni a EN_PROGRESO.
- La fecha de creación se asigna sola al crear la tarea.
- La fecha de actualización se actualiza sola cada vez que se modifica.

---

# Nivel 3 - Preguntas Teóricas

## 1. ¿Cuál es la diferencia entre @RestController y @Controller?

`@Controller` se usa cuando uno hace una página web con vistas (por ejemplo con Thymeleaf). El método retorna el nombre de una vista HTML.

`@RestController` se usa cuando uno hace una API que devuelve JSON. No retorna vistas, retorna datos.

Por dentro, `@RestController` es lo mismo que poner `@Controller` + `@ResponseBody` en cada método. En este proyecto usé `@RestController` porque la API devuelve JSON.

## 2. ¿Por qué se usan DTOs en lugar de exponer directamente la entidad JPA?

Por varias razones:

- **Seguridad:** la entidad puede tener campos que el usuario no debería poder cambiar (como las fechas que se asignan solas). Con un DTO yo controlo qué campos acepto.

- **Validaciones:** en el DTO puedo poner `@NotBlank`, `@Size(min = 3)`, etc. Esas validaciones son del lado del usuario, no de la base de datos.

- **Si cambia la entidad, no se rompe la API:** si mañana le agrego un campo a la entidad, mi API sigue funcionando igual. El DTO es como un "intermediario" entre el cliente y la base de datos.

## 3. ¿Qué ventaja tiene @PrePersist sobre asignar la fecha en el constructor?

Si pongo la fecha en el constructor (`new Tarea()`), la fecha se asigna cuando creo el objeto en Java. Pero entre ese momento y cuando la tarea se guarda en la base de datos puede pasar tiempo.

Con `@PrePersist`, la fecha se asigna justo antes de guardar en la base de datos. Es más exacto.

Además, no necesito acordarme de poner la fecha cada vez que creo una tarea. La entidad sola se encarga.

## 4. ¿Qué diferencia hay entre ddl-auto=update y ddl-auto=create? ¿Cuál usarías en producción?

- `create`: cada vez que arranca la aplicación, **borra todas las tablas** y las vuelve a crear. Sirve para empezar limpio pero pierdes los datos.

- `update`: solo aplica los cambios necesarios (agrega columnas nuevas, etc.) pero **NO borra datos**.

En producción **no usaría ninguno de los dos**. Usaría `validate` o `none`, porque:

- `create` borraría toda la data real.
- `update` puede hacer cambios automáticos en la base de datos que uno no controla.

En producción los cambios se manejan con scripts SQL o con herramientas como Flyway, que llevan el control de cada cambio que se le hace a la base de datos.

## 5. Si esta API fuera a producción con usuarios reales, ¿qué 3 cambios harías?

1. **Agregar autenticación con JWT.** Ahora cualquier persona puede crear, modificar o eliminar tareas. En producción cada usuario tendría que iniciar sesión y solo ver SUS tareas.

2. **Sacar la contraseña del application.yaml.** Nunca se debe subir un proyecto con la contraseña real de la base de datos. Usaría variables de entorno.

3. **Cambiar `ddl-auto: update` por `validate`.** Y usar Flyway para controlar los cambios de la base de datos con scripts versionados, en vez de dejar que Hibernate los haga solo.

Otros cambios que también haría: usar HTTPS, agregar paginación a todos los listados, configurar bien el CORS, y agregar logs para saber qué pasa cuando hay errores.
