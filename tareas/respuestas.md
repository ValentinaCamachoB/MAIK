# RESPUESTAS - Nivel 3 (Preguntas Teoricas)

## 1. ¿Cual es la diferencia entre @RestController y @Controller en Spring Boot?

`@Controller` es la anotacion clasica de Spring MVC, se usa cuando uno trabaja con vistas (por ejemplo paginas HTML hechas con Thymeleaf). Cuando un metodo retorna un String, Spring entiende que ese String es el nombre de la vista que tiene que mostrar.

`@RestController` es como una version recortada de `@Controller` pero pensada para APIs REST. Internamente es lo mismo que poner `@Controller` + `@ResponseBody`, lo que significa que todo lo que retorne el metodo se convierte automaticamente en JSON (o XML), no busca ninguna vista.

En este proyecto usamos `@RestController` porque la aplicacion devuelve JSON, no paginas web.

---

## 2. ¿Por que se usan DTOs en lugar de exponer directamente la entidad JPA?

Por varias razones que vi en este proyecto:

- **Seguridad**: la entidad puede tener campos sensibles o internos (por ejemplo una contrasena, o el campo `fechaActualizacion` que el cliente NO deberia poder modificar). Si expones la entidad directamente, alguien podria mandarte esos campos en el JSON y los cambiaria.

- **Validaciones diferentes**: en el DTO de entrada (`TareaRequestDTO`) yo puedo pedir que el titulo tenga minimo 3 caracteres con `@Size(min = 3)`, pero la entidad no necesita esa validacion porque esa restriccion es del lado del usuario, no de la base de datos.

- **Acoplamiento**: si manana cambio la entidad para agregar o quitar un campo, eso no deberia romper a quien consume mi API. El DTO actua como un "intermediario" que mantiene el contrato de la API estable aunque la entidad cambie.

- **Datos no necesarios**: a veces la entidad tiene relaciones con otras tablas que generarian un JSON enorme y con loops infinitos. El DTO me deja escoger exactamente que campos exponer.

---

## 3. ¿Que ventaja tiene @PrePersist sobre asignar la fecha en el constructor de la entidad?

Si pongo la fecha en el constructor, esa fecha se asigna en el momento en que CREO el objeto Java con `new Tarea()`. Pero entre ese momento y el momento en que la tarea se guarda en la base de datos puede pasar un rato (validaciones, logica del servicio, conversiones, etc.). Entonces la fecha que queda no es la "fecha real de creacion en la base de datos".

`@PrePersist` se ejecuta JUSTO ANTES de que Hibernate inserte el registro en la base de datos. Asi me aseguro que la fecha es exactamente cuando se guardo, no cuando se instancio el objeto.

Otra ventaja: con `@PrePersist` la entidad es la responsable de su propia fecha, no necesito acordarme de setearla en cada lugar donde cree una tarea. Si manana creo tareas desde un script, desde otro servicio o desde donde sea, la fecha siempre se va a asignar bien.

---

## 4. ¿Que diferencia hay entre spring.jpa.hibernate.ddl-auto=update y ddl-auto=create? ¿Cual usarias en produccion y por que?

- `create`: cada vez que arranca la aplicacion, Hibernate **borra todas las tablas y las vuelve a crear desde cero**. Sirve para pruebas o para empezar siempre con la base limpia, pero **te borra todos los datos**.

- `update`: Hibernate compara las entidades con las tablas que ya existen en la base de datos y aplica solo los cambios necesarios (agrega columnas nuevas, etc.). NO borra datos. Si no encuentra la tabla, la crea.

**En produccion NO usaria ninguno de los dos. Usaria `validate` o `none`**.

¿Por que?
- `create` borraria toda la data real cada vez que se reinicia el servidor. Seria un desastre.
- `update` parece seguro pero tampoco esta bien, porque puede hacer cambios automaticos en la base de datos que uno no controla. Por ejemplo, si por error renombro un campo en la entidad, Hibernate podria agregar la columna nueva pero dejar la vieja con los datos huerfanos.

En produccion los cambios de la base de datos se manejan con scripts SQL controlados o con herramientas como **Flyway** o **Liquibase**, que llevan un historial de cada cambio (migracion). Asi uno tiene control total y puede revisar cada cambio antes de aplicarlo.

---

## 5. Si esta API fuera a produccion con usuarios reales, menciona al menos 3 cambios que harias en la configuracion o arquitectura.

1. **Agregar autenticacion y autorizacion (Spring Security + JWT)**: ahora mismo cualquier persona que conozca la URL puede crear, modificar o eliminar tareas. En produccion cada usuario deberia tener que iniciar sesion y solo deberia poder manejar SUS tareas. Esto implicaria agregar una tabla de usuarios, una entidad Usuario, una relacion Usuario-Tarea, y proteger los endpoints.

2. **Sacar las credenciales del application.yaml**: nunca se debe subir un repositorio con la URL, usuario y contrasena de la base de datos visibles. En produccion usaria variables de entorno o un servicio como AWS Secrets Manager / Azure Key Vault. El application.yaml leeria los valores con `${DB_URL}`, `${DB_PASSWORD}`, etc.

3. **Cambiar `ddl-auto: update` por `validate` y usar Flyway**: como explique en la pregunta 4, dejar que Hibernate modifique la base de datos automaticamente es riesgoso. Lo correcto es controlar los cambios con scripts de migracion versionados.

Otros cambios que tambien haria:
- **HTTPS obligatorio**: configurar el servidor para que solo acepte conexiones cifradas.
- **Paginacion en TODOS los listados**: con el tiempo la tabla puede tener miles de tareas y traer todas seria un problema de memoria. Habria que limitar siempre a, por ejemplo, 20 resultados por pagina.
- **Logging y monitoreo**: agregar logs con SLF4J (Logback) y una herramienta como Prometheus + Grafana para monitorear errores y rendimiento.
- **Manejo de CORS adecuado**: configurar exactamente que dominios pueden consumir la API, no dejar `*`.
- **Rate limiting**: para evitar que alguien sature la API con miles de peticiones.
- **Variables de entorno por ambiente**: tener `application-dev.yaml`, `application-prod.yaml`, `application-test.yaml`, con configuraciones distintas.
