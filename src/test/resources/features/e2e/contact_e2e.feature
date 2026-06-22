 # language: es
@e2e
Característica: Flujo E2E de contactos

  Escenario: Crear contacto por API, validar en UI, eliminar por API y validar ausencia
    Dado que creo un contacto por API con nombre "Ana" email "ana@example.com" phone "+123456789"
    Cuando abro la interfaz de contactos en la UI
    Entonces debo ver el contacto con email "ana@example.com" en la lista
    Cuando elimino el contacto por API
    Entonces no debo ver el contacto con email "ana@example.com" en la lista
