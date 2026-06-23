# language: es
Característica: API de creación de usuarios
  Como consumidor del API de usuarios
  Quiero crear un nuevo usuario autenticado
  Para poder gestionar contactos

  @api @smoke @adduser
  Escenario: Crear usuario con token válido
    Dado el servicio de autenticación está disponible
    Cuando el usuario solicita autenticación con credenciales válidas
    Y crea un nuevo usuario vía API con datos válidos
    Entonces el adduser debe devolver código 200
    Y la respuesta contendrá el email creado
