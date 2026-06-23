# language: es

@e2e @contactos
Característica: Gestión de contactos

  Escenario: Crear contacto desde UI usando usuario creado por API

    Dado que existe un usuario creado mediante API
    Y inicia sesión desde UI
    Cuando crea un contacto desde UI
    Entonces el contacto debe visualizarse en Contact List

  Escenario: CRUD completo contacto

    Dado que existe un usuario creado mediante API
    Y inicia sesión desde UI
    Cuando crea un contacto desde UI
    Entonces la API debe retornar el contacto creado
    Cuando actualiza el contacto desde UI
    Entonces la API debe retornar la información actualizada
    Cuando elimina el contacto desde UI
    Entonces la API no debe encontrar el contacto
