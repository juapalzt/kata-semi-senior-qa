# language: es

@ui @usuarios
Característica: Gestión de usuarios desde la interfaz

  Como visitante de la aplicación
  Quiero registrarme e iniciar sesión
  Para administrar mis contactos

  Escenario: Registrar usuario exitosamente

    Dado que el usuario ingresa a la página Login
    Cuando selecciona "Not yet a user? Click here to sign up!"
    Entonces debe visualizar la página Add User

    Cuando diligencia los datos obligatorios
    Y presiona Submit

    Entonces debe visualizar Contact List
    Y debe visualizar Add a New Contact
    Y debe visualizar Logout

  Escenario: Cancelar registro de usuario

    Dado que el usuario se encuentra en Add User
    Cuando presiona Cancel
    Entonces debe regresar a Login

  Escenario: Iniciar sesión exitosamente

    Dado que existe un usuario registrado en la UI
    Y el usuario se encuentra en Login

    Cuando ingresa credenciales válidas
    Y presiona Submit

    Entonces debe visualizar Contact List
    Y debe visualizar Add a New Contact
    Y debe visualizar Logout

  Escenario: Cerrar sesión exitosamente

    Dado que existe una sesión activa

    Cuando selecciona Logout

    Entonces debe regresar a Login
