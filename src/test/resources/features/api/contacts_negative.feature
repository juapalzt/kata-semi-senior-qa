# language: es

@api @contactos @negativo
Característica: Validaciones de contactos

  Escenario: Crear contacto sin nombre

    Dado que existe un usuario autenticado
    Cuando intenta crear un contacto inválido
    Entonces la respuesta debe indicar error

  Escenario: Obtener contacto inexistente

    Dado que no existe el contacto
    Cuando consulta el contacto por id
    Entonces la respuesta debe retornar código 404

  Escenario: Eliminar contacto inexistente

    Dado que no existe el contacto
    Cuando elimina el contacto
    Entonces la respuesta debe retornar código 404
