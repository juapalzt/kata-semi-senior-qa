# language: es

@ui @navegacion
Característica: Navegación de la aplicación

  Escenario: Navegar desde Login a Sign Up

    Dado que el usuario se encuentra en Login

    Cuando selecciona Sign Up

    Entonces debe visualizar Add User

  Escenario: Regresar desde Add User a Login

    Dado que el usuario se encuentra en Add User

    Cuando selecciona Cancel

    Entonces debe visualizar Login

  Escenario: Regresar desde Contact Details a Contact List

    Dado que el usuario se encuentra en Contact Details

    Cuando selecciona Return to Contact List

    Entonces debe visualizar Contact List

  Escenario: Cancelar creación de contacto

    Dado que el usuario se encuentra en Add Contact

    Cuando selecciona Cancel

    Entonces debe regresar a Contact List
