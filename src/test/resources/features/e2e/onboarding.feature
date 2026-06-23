# language: es

@e2e @smoke @critico
Característica: Incorporación de usuario

  Como usuario nuevo
  Quiero acceder a la aplicación
  Para administrar mis contactos

  Escenario: Crear usuario por API e iniciar sesión por UI

    Dado que se crea un usuario mediante API
    Cuando el usuario accede a Login
    Y realiza el inicio de sesión desde UI
    Entonces debe visualizar Contact List
    Y debe visualizar Add a New Contact
    Y debe visualizar Logout
