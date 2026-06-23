# language: es

@ui @negativo
Característica: Validaciones funcionales

  Escenario: Login con credenciales inválidas

    Dado que el usuario se encuentra en Login

    Cuando ingresa credenciales inválidas

    Y presiona Submit

    Entonces debe visualizar un mensaje de error

  Escenario: Crear usuario con información incompleta

    Dado que el usuario se encuentra en Add User

    Cuando diligencia información obligatoria incompleta

    Y presiona Submit

    Entonces debe visualizar un mensaje de validación

  Escenario: Crear contacto con campos obligatorios vacíos

    Dado que el usuario se encuentra en Add Contact

    Cuando intenta guardar el contacto sin completar los datos requeridos

    Entonces debe visualizar un mensaje de validación
