# language: es

@e2e @full @critico @regresion
Característica: Flujo completo de negocio

  Escenario: Administrar usuario y contactos de extremo a extremo

    Dado que se crea un usuario mediante API
    Cuando inicia sesión desde UI
    Y crea un contacto desde UI
    Entonces la API debe retornar el contacto creado
    Cuando actualiza el contacto mediante API
    Entonces los cambios deben visualizarse en UI
    Cuando elimina el contacto desde UI
    Entonces la API no debe encontrar el contacto
    Cuando elimina el usuario mediante API
    Entonces la cuenta debe eliminarse correctamente
