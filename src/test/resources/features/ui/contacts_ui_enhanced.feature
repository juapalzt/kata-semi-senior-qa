# language: es

@ui @contactos
Característica: Administración de contactos

  Como usuario autenticado
  Quiero administrar mis contactos
  Para mantener actualizada mi agenda

  Escenario: Crear contacto exitosamente

    Dado que el usuario se encuentra en Contact List

    Cuando selecciona Add a New Contact

    Entonces debe visualizar Add Contact

    Cuando diligencia la información requerida

    Y presiona Submit

    Entonces el contacto debe visualizarse en Contact List

  Escenario: Visualizar detalle de contacto

    Dado que existe un contacto registrado

    Cuando selecciona el contacto

    Entonces debe visualizar Contact Details

  Escenario: Editar contacto exitosamente

    Dado que el usuario se encuentra en Contact Details

    Cuando selecciona Edit Contact

    Entonces debe visualizar el formulario de edición

    Cuando modifica la información

    Y presiona Submit

    Entonces los cambios deben persistirse

  Escenario: Eliminar contacto exitosamente

    Dado que el usuario se encuentra en Contact Details

    Cuando selecciona Delete Contact

    Entonces el contacto no debe aparecer en Contact List
