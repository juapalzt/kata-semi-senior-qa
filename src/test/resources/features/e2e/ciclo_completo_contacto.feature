# language: es
Característica: Ciclo completo de gestión de contactos
  Como usuario autenticado del sistema
  Quiero realizar un flujo completo de gestión de contactos
  Para validar la integración entre API y UI en todas las operaciones

  @e2e @smoke @importante
  Escenario: Administrar un contacto de principio a fin
    # SETUP: Autenticación
    Dado que el usuario ha iniciado sesión exitosamente

    # FASE 1: Crear contacto desde UI y validar en API
    Cuando crea un nuevo contacto desde la interfaz con nombre "Juan" email "juan.e2e@test.com"
    Entonces el contacto debe aparecer en la lista de contactos UI
    Y el contacto debe existir en la API

    # FASE 2: Actualizar contacto desde UI y validar en API
    Cuando actualiza la información del contacto para cambiar el nombre a "Juan Carlos"
    Entonces la UI debe mostrar los datos actualizados
    Y la API debe retornar la información actualizada

    # FASE 3: Eliminar contacto desde UI y validar ausencia en API
    Cuando elimina el contacto desde la interfaz
    Entonces el contacto no debe aparecer en la lista de contactos UI
    Y la API no debe encontrar el contacto
