-- Este archivo se ejecuta AUTOMATICAMENTE al arrancar Spring Boot
-- Inserta datos de prueba en la tabla "tareas".

-- Primero limpiamos la tabla por si tiene datos viejos
DELETE FROM tareas;

ALTER TABLE tareas AUTO_INCREMENT = 1;

INSERT INTO tareas (titulo, descripcion, estado, prioridad, fecha_creacion, fecha_actualizacion)
VALUES
('Entregar prueba tecnica', 'Subir el repositorio al GitHub y enviar el link', 'PENDIENTE', 'URGENTE', NOW(), NOW()),
('Estudiar Spring Boot', 'Repasar JPA y manejo de excepciones', 'EN_PROGRESO', 'ALTA', NOW(), NOW()),
('Hacer mercado', 'Comprar arroz, huevos y pan', 'PENDIENTE', 'MEDIA', NOW(), NOW()),
('Revisar correo electronico', 'Revisar correos del trabajo', 'COMPLETADA', 'BAJA', NOW(), NOW()),
('Inscribirse al gimnasio', 'Buscar gimnasio cerca de la casa', 'CANCELADA', 'BAJA', NOW(), NOW());