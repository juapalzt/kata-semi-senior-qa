# language: es

@e2e @security
Característica: Seguridad de sesión

  Escenario: Impedir acceso después de logout

    Dado que existe una sesión activa
    Cuando realiza Logout
    Entonces debe regresar a Login
    Cuando intenta acceder nuevamente a Contact List
    Entonces debe solicitar autenticación
