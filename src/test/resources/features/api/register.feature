# language: es
Característica: API de registro de usuarios
  Como consumidor del API de registro
  Quiero registrar un nuevo usuario con mis credenciales
  Para poder usar la aplicación

  @api @smoke
  Escenario: Registro exitoso por API
    Dado el servicio de registro está disponible
    Cuando el usuario solicita registro con datos válidos
    Entonces el registro debe devolver código 200
    Y la respuesta deberá contener los datos del usuario registrado
