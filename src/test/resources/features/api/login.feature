# language: es
Característica: API de autenticación
  Como consumidor del API de autenticación
  Quiero autenticarme con credenciales válidas
  Para recibir un token que permita llamadas autenticadas

  @api @smoke
  Escenario: Login exitoso
    Dado que el servicio de autenticación está disponible
    Cuando el usuario solicita autenticación con credenciales válidas
    Entonces la respuesta tendrá el código 200
    Y la respuesta contendrá un token de autenticación
