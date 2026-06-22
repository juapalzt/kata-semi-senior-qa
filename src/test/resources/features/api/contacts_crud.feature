# language: es
Característica: CRUD de la API de contactos
  Como consumidor del API
  Quiero autenticarme, crear un contacto, recuperarlo y eliminarlo
  Para que el flujo de la API de contactos funcione de extremo a extremo

  @api @smoke
  Escenario: Autenticar, crear, obtener y eliminar un contacto
    Dado que el servicio de autenticación está disponible
    Cuando el usuario se autentica con credenciales válidas
    Y el usuario crea un nuevo contacto
    Entonces el contacto se crea correctamente
    Cuando el usuario recupera el contacto creado
    Entonces se retornan los detalles del contacto
    Cuando el usuario elimina el contacto creado
    Entonces el contacto se elimina correctamente

  @api @smoke
  Escenario: Autenticar, crear, actualizar, obtener y eliminar un contacto
    Dado que el servicio de autenticación está disponible
    Cuando el usuario se autentica con credenciales válidas
    Y el usuario crea un nuevo contacto
    Entonces el contacto se crea correctamente
    Cuando el usuario actualiza el nombre del contacto creado a "Contacto Actualizado"
    Entonces la actualización del contacto es aceptada
    Cuando el usuario recupera el contacto creado
    Entonces se retornan los detalles del contacto
    Y el nombre del contacto debe ser "Contacto Actualizado"
    Cuando el usuario elimina el contacto creado
    Entonces el contacto se elimina correctamente
