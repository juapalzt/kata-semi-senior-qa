Feature: Registro por API y login por UI
  Como usuario de la plataforma
  Quiero registrar un usuario mediante API y luego iniciar sesión en la interfaz web
  Para validar el flujo completo de extremo a extremo

  @api @ui @smoke
  Scenario: Registrar un usuario vía API y luego hacer login en la UI
    Given el servicio de registro está disponible
    When crea un nuevo usuario vía API con datos válidos
    Then el adduser debe devolver código 200
    And la respuesta contendrá el email creado
    When el usuario inicia sesión con las credenciales creadas por API
    Then el login UI debe ser exitoso
