@ui
Feature: Registro, Login y Logout UI

  Scenario: Usuario se registra, se loguea y cierra sesión correctamente
    Given que un usuario nuevo quiere registrarse
    When completa el formulario de registro con nombre "Juan" email "juan@example.com" password "P@ssw0rd"
    Then el registro debe ser exitoso
    When el usuario intenta iniciar sesión con email "juan@example.com" y password "P@ssw0rd"
    Then el usuario debe verse autenticado en la pantalla de inicio
    When el usuario cierra sesión
    Then el usuario no debe estar autenticado
