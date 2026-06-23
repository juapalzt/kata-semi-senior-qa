# language: es

@api @usuarios
Característica: Gestión de usuarios

  Como consumidor de la API
  Quiero administrar usuarios
  Para acceder a la aplicación

  Escenario: Crear usuario exitosamente

    Dado que existe información válida de usuario
    Cuando crea un usuario mediante API
    Entonces la respuesta debe retornar código 201
    Y el usuario debe ser creado correctamente

  Escenario: Iniciar sesión exitosamente

    Dado que existe un usuario registrado
    Cuando realiza login mediante API
    Entonces la respuesta debe retornar código 200
    Y debe generarse un token JWT válido

  Escenario: Obtener perfil del usuario

    Dado que existe un usuario autenticado
    Cuando consulta su perfil
    Entonces debe obtener la información del usuario

  Escenario: Actualizar información del usuario

    Dado que existe un usuario autenticado
    Cuando actualiza su información personal
    Entonces la información debe persistirse correctamente

  Escenario: Cerrar sesión

    Dado que existe un usuario autenticado
    Cuando realiza logout
    Entonces la respuesta debe retornar código 200

  Escenario: Eliminar usuario

    Dado que existe un usuario autenticado
    Cuando elimina su cuenta
    Entonces la respuesta debe retornar código 200
