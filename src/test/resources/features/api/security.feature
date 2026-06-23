# language: es

@api @security
Característica: Seguridad y autenticación

  Como consumidor de la API
  Quiero validar controles de acceso
  Para garantizar la seguridad del sistema

  Escenario: Consultar perfil sin token

    Dado que el usuario no posee token
    Cuando consulta su perfil
    Entonces la respuesta debe retornar código 401

  Escenario: Consultar contactos sin token

    Dado que el usuario no posee token
    Cuando consulta la lista de contactos
    Entonces la respuesta debe retornar código 401

  Escenario: Consultar contactos con token inválido

    Dado que existe un token inválido
    Cuando consulta la lista de contactos
    Entonces la respuesta debe retornar código 401

  Escenario: Login con contraseña incorrecta

    Dado que existe un usuario registrado
    Cuando intenta iniciar sesión con contraseña incorrecta
    Entonces la respuesta debe retornar código 401

  Escenario: Login con usuario inexistente

    Dado que no existe el usuario
    Cuando intenta iniciar sesión
    Entonces la respuesta debe retornar código 401
