 # language: es
@ui
Característica: Flujo de la lista de contactos

  Escenario: Usuario crea, visualiza, edita y elimina un contacto
    Dado que el usuario abre la lista de contactos
    Cuando crea un contacto con nombre "Carlos" apellidos "Gomez" email "carlos@example.com" telefono "+34123456789" ciudad "Madrid" pais "España"
    Entonces ve el contacto con email "carlos@example.com" en la lista
    Cuando visualiza el contacto con email "carlos@example.com"
    Entonces ve los detalles del contacto con nombre "Carlos Gomez" y email "carlos@example.com"
    Cuando edita el contacto con email "carlos@example.com" para cambiar el nombre a "Carlos Ruiz"
    Entonces ve el contacto con email "carlos@example.com" actualizado con nombre "Carlos Ruiz"
    Cuando elimina el contacto con email "carlos@example.com"
    Entonces no debe ver el contacto con email "carlos@example.com" en la lista
