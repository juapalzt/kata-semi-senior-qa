# language: es
Característica: Inicio de sesión
  Como usuario registrado
  Quiero iniciar sesión
  Para acceder a la lista de contactos

  @ui @login @smoke
  Escenario: Inicio de sesión exitoso
    Dado que el usuario se encuentra en la página de inicio de sesión
    Cuando ingresa credenciales válidas
    Y presiona el botón iniciar sesión
    Entonces debe visualizar la lista de contactos
    Y debe visualizar el botón de cerrar sesión
