Pasos para ejecutar la aplicacion .

-Ejecutar el programa

Abre una terminal en la carpeta del proyecto y ejecuta:

mvn spring-boot:run

Cuando veas en la consola algo como:

Started FootballApplication in X.XXX seconds

ya esta corriendo en `http://localhost:1234`.

-Probar en Postman

Guardar el entrenamiento 1

- Metodo: `POST`
- URL:`http://localhost:1234/trainings/save`
- Body → raw → JSON

{
  "trainingNumber": 1,
  "players": [
    { "playerName": "Jugador1", "shotPower": 10, "speed": 5, "effectivePasses": 25 },
    { "playerName": "Jugador2", "shotPower": 16, "speed": 5, "effectivePasses": 20 },
    { "playerName": "Jugador3", "shotPower": 15, "speed": 3, "effectivePasses": 30 },
    { "playerName": "Jugador4", "shotPower": 12, "speed": 4, "effectivePasses": 18 },
    { "playerName": "Jugador5", "shotPower": 11, "speed": 3, "effectivePasses": 19 },
    { "playerName": "Jugador6", "shotPower": 9, "speed": 3, "effectivePasses": 22 },
    { "playerName": "Jugador7", "shotPower": 10, "speed": 2, "effectivePasses": 24 }
  ]
}

Haz clic en Send.

Repetir con los entrenamientos 2 y 3

Cambia solo el valor de `"trainingNumber"` a `2` y envia.
Despues cambialo a `3` y envia otra vez.

Obtener el equipo titular

- Metodo: `GET`
- URL: `http://localhost:1234/trainings/starting-team`

Haz clic en Send. Veras los 5 mejores jugadores con su puntaje.

