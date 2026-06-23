# language: es

@api @usuarios @negativo
Característica: Validaciones de usuarios

  Escenario: Crear usuario con email duplicado

    Dado que existe un usuario registrado
    Cuando intenta crear otro usuario con el mismo email
    Entonces la respuesta debe indicar error

  Escenario: Crear usuario sin email

    Dado que existe información incompleta
    Cuando crea un usuario mediante API
    Entonces la respuesta debe indicar error

  Escenario: Crear usuario sin contraseña

    Dado que existe información incompleta
    Cuando crea un usuario mediante API
    Entonces la respuesta debe indicar error
