# Framework de Automatización — kata-semi-senior-qa

**Arquitectura**

- Estructura modular basada en Screenplay (Actors, Abilities, Tasks, Questions, Interactions)
- Capas principales:
  - `api`: clientes, endpoints y DTOs para interacciones REST.
  - `core`: abilities (CallApiAbility), tasks, interactions y preguntas (Questions).
  - `ui`: Page Objects (`pages`), componentes reutilizables y localizadores.
  - `tests`: definiciones de pasos (stepdefinitions), hooks y suites/runner Cucumber.

El diseño busca mantener Tasks y Questions sin estado y reutilizar la `Ability` para llamadas API (manejo centralizado de tokens y cabeceras).

**Patrones y decisiones arquitectónicas**

- Screenplay Pattern (Serenity): separa comportamiento (Tasks) de verificaciones (Questions) y modela actores que pueden tener abilities.
- Page Object Pattern (PO): `ui/pages` expone `Target` para elementos interactuables.
- DTOs inmutables/POJOs para requests/responses (package `domain.models`) y mapeo con Jackson.
- Centralización de endpoints en `ApiEndpoints` para evitar cadenas duras.
- `CallApiAbility`: wrapper sobre `CallAnApi` para inyectar `Authorization` y reusar token entre Tasks.
- Uso de Cucumber/Gherkin para features legibles y ejecutables por stakeholders.

**Dependencias clave (Maven)**

- Java: 17
- Serenity BDD: 3.8.13
- serenity-cucumber8 (Cucumber 8.18.0)
- Rest Assured: 5.4.0
- Jackson (Databind): 2.15.2
- Lombok: 1.18.30
- JUnit: 4.13.3

Consulta el `pom.xml` del proyecto para la lista completa de dependencias y versiones.

**Estructura de carpetas (resumen)**

- `src/main/java`: código del framework (api, core, ui, shared, domain)
- `src/test/java`: step definitions, hooks y runners
- `src/test/resources/features`: archivos Gherkin (api, ui, e2e)
- `target/site/serenity`: reportes HTML generados por Serenity

**Ejecución**

- Compilar (sin ejecutar tests):
  ```bash
  mvn -DskipTests test
  ```

- Ejecutar todos los tests (local):
  ```bash
  mvn clean verify
  ```

- Ejecutar por tag Cucumber (ej. escenarios E2E):
  ```bash
  mvn clean verify -Dcucumber.filter.tags="@e2e"
  ```

- Ejecutar un test o clase específica (ejemplo rápido):
  ```bash
  mvn -Dtest=*ContactE2ESteps test
  ```

**Parámetros / Variables de entorno útiles**

- `-Dapi.base.url` — URL base para llamadas API (por defecto `http://localhost:8080`).
- `-Dui.base.url` — URL base de la aplicación UI (por defecto `http://localhost:8080`).
- `-Dauth.token` — token a usar si quieres inicializar escenarios con autenticación.
- `-Dwebdriver.driver` — driver para Serenity (`chrome`, `firefox`, etc.).
- `-Dserenity.headless=true` — ejecutar navegadores en modo headless (opcional).

Ejemplo ejecutando sólo E2E con Chrome headless y base URLs:
```bash
mvn clean verify -Dcucumber.filter.tags="@e2e" -Dapi.base.url="http://api.local" -Dui.base.url="http://app.local" -Dwebdriver.driver=chrome -Dserenity.headless=true
```

**Reportes**

- Serenity genera reportes HTML y screenshots en: `target/site/serenity/index.html`.
- Para integrar en CI: archivar `target/site/serenity` como artefacto y exponer `index.html`.

**Casos cubiertos (resumen)**

- API:
  - `Login API`: validar respuesta y persistencia de token.
  - `Contacts CRUD`: crear, consultar y eliminar contactos vía API.

- UI:
  - `Login UI`: page object `LoginPage` y task `Login.withCredentials`.
  - `Register UI`: formulario de registro y flujo Register→Login→Logout.
  - `HomePage`: validaciones de usuario autenticado.

- E2E:
  - `contact_e2e.feature` (tag `@e2e`): Crear contacto por API → abrir UI → validar existencia en la lista → eliminar por API → validar desaparición.

**Buenas prácticas y recomendaciones**

- Mantén Tasks y Questions lo más puras posibles: que no almacenen estado.
- Centraliza configuración en `src/test/resources/environments` y via system properties.
- Prefiere ejecutar suites con tags (`@api`, `@ui`, `@e2e`) para pipelines CI eficientes.

**Siguientes mejoras sugeridas**

- Integración con Docker para entornos reproducibles (SUT + BrowserDriver).
- Añadir tests paralelizables y estrategias de datos (data builders + test data isolation).
- Mejorar manejo de retries y flakiness para UI (esperas explícitas y reintentos prudentes).

Si quieres, puedo:
- Añadir un `README` en español/inglés adicional o traducir este archivo.
- Incluir comandos concretos para GitHub Actions / Azure DevOps para ejecutar y publicar reportes.
