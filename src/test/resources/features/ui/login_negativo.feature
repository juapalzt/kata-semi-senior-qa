# language: es
Característica: Inicio de sesión inválido
  Como usuario del sistema
  Quiero intentar iniciar sesión con credenciales incorrectas
  Para validar que el sistema rechaza accesos no autorizados

  @ui @login @negativo @smoke
  Escenario: Intentar iniciar sesión con credenciales incorrectas
    Dado que el usuario se encuentra en la página de inicio de sesión
    Cuando ingresa credenciales inválidas
    Y presiona el botón iniciar sesión
    Entonces debe visualizar un mensaje de error
    Y debe permanecer en la página de inicio de sesión
