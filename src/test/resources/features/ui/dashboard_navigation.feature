@ui @dashboard @navigation
Feature: Dashboard y navegación principal
  Como usuario autenticado
  Quiero acceder al dashboard y navegar entre los módulos principales
  Para verificar la experiencia de navegación de la aplicación web

  Scenario: Acceso al dashboard y navegación al módulo de contactos
    Given que el usuario se encuentra en la página de inicio de sesión
    When ingresa credenciales válidas
    And presiona el botón iniciar sesión
    Then debe visualizar la lista de contactos
    When navega al módulo de contactos
    Then debe visualizar el listado de contactos
    And debe visualizar el botón de cerrar sesión
