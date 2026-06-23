# Framework de Automatización — kata-semi-senior-qa

<<<<<<< HEAD
Resumen profesional y guía rápida del framework de automatización diseñado para pruebas API y UI con Serenity Screenplay, Rest Assured y Gherkin.

## Arquitectura
=======
**Arquitectura**
>>>>>>> 060adbc8b6476c6f3ae942a8d6d0079ccf3e122c

- Modular y basada en Screenplay: Actors, Abilities, Tasks, Questions e Interactions.
- Capas principales:
  - `api`: clientes, endpoints y DTOs.
  - `core`: abilities, tasks, interactions y questions.
  - `ui`: Page Objects, componentes y localizadores.
  - `tests`: step definitions, hooks y runners Cucumber.
- Diseño orientado a mantener Tasks y Questions sin estado y centralizar configuración y autorización.

## Patrones

- Screenplay: separa acciones de verificaciones.
- Page Object: `ui/pages` expone `Target` para los elementos.
- DTOs/POJOs para requests/responses con Jackson.
- Endpoints centralizados en `ApiEndpoints`.
- `CallApiAbility` para reutilizar token y headers en llamadas API.

## Dependencias clave

- Java 17
- Serenity BDD 3.8.13
- serenity-cucumber8 (Cucumber 8.18.0)
- Rest Assured 5.4.0
- Jackson Databind 2.15.2
- Lombok 1.18.30
- JUnit 4.13.3

Revisa `pom.xml` para la lista completa de dependencias.

## Estructura

- `src/main/java`: framework (api, core, ui, shared, domain)
- `src/test/java`: step definitions, hooks y runners
- `src/test/resources/features`: Gherkin (api, ui, e2e)
- `target/site/serenity`: reportes HTML de Serenity

## Ejecución

- Compilar sin tests:
  ```bash
  mvn -DskipTests test
  ```
- Ejecutar todos los tests:
  ```bash
  mvn clean verify
  ```
- Ejecutar por tag:
  ```bash
  mvn clean verify -Dcucumber.filter.tags="@ui"
  ```
- Ejecutar una clase específica:
  ```bash
  mvn -Dtest=*ContactE2ESteps test
  ```

## Parámetros útiles

- `-Dapi.base.url=...`
- `-Dui.base.url=...`
- `-Dauth.token=...`
- `-Dwebdriver.driver=chrome`
- `-Dserenity.headless=true`

## Reportes y evidencias

- Reportes: `target/site/serenity/index.html`
- Evidencia organizada por ejecución en:
  - `target/serenity-reports/evidences/{YYYY-MM-DD_HH-mm-ss}/UI/...`
- También se generan copias en:
  - `target/serenity-reports/site/{timestamp}/`
  - `target/serenity-reports/surefire-reports/{timestamp}/`
- Para detalles de organización consulta `EVIDENCE_ORGANIZATION.md`.

## Casos cubiertos

- API:
  - Login y persistencia de token.
  - Contacts CRUD.
- UI:
  - Login exitoso y login inválido.
  - Registro, login y logout.
  - Validaciones de usuario autenticado.
- E2E:
  - Flujo de contacto: crear por API, validar en UI, eliminar por API.

## Nota
Solución simple, modular y lista para ejecutar en local. Mantiene evidencia organizada y comandos claros para presentar.
