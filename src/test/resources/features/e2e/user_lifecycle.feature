# language: es

@e2e @usuarios
Característica: Ciclo de vida de usuario

  Escenario: Administrar usuario de extremo a extremo

    Dado que se crea un usuario mediante API
    Cuando inicia sesión desde UI
    Entonces debe visualizar Contact List
    Cuando realiza Logout desde UI
    Entonces debe regresar a Login
    Cuando elimina el usuario mediante API
    Entonces la cuenta debe eliminarse correctamente
