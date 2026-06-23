# language: es

@api @contactos
Característica: Gestión de contactos

  Como usuario autenticado
  Quiero administrar contactos
  Para mantener mi agenda actualizada

  Escenario: Crear contacto exitosamente

    Dado que existe un usuario autenticado
    Cuando crea un contacto
    Entonces la respuesta debe retornar código 201
    Y el contacto debe almacenarse correctamente

  Escenario: Obtener lista de contactos

    Dado que existe un usuario autenticado
    Cuando consulta la lista de contactos
    Entonces la respuesta debe retornar código 200
    Y debe obtener una lista válida

  Escenario: Obtener contacto por identificador

    Dado que existe un contacto registrado
    Cuando consulta el contacto por id
    Entonces debe obtener la información correcta

  Escenario: Actualizar completamente un contacto

    Dado que existe un contacto registrado
    Cuando actualiza todos los datos del contacto
    Entonces la información debe persistirse correctamente

  Escenario: Actualizar parcialmente un contacto

    Dado que existe un contacto registrado
    Cuando actualiza parcialmente el contacto
    Entonces la información debe persistirse correctamente

  Escenario: Eliminar contacto

    Dado que existe un contacto registrado
    Cuando elimina el contacto
    Entonces la respuesta debe retornar código 200
    Y el contacto no debe existir posteriormente
