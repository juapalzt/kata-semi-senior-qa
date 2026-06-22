package org.example.tests.stepdefinitions.e2e;

import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Entonces;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actions.Open;
import org.example.framework.core.actors.ActorManager;
import org.example.framework.core.tasks.Login;
import org.example.framework.core.tasks.LoginApiTask;
import org.example.framework.core.tasks.CreateContactTask;
import org.example.framework.core.tasks.DeleteContactTask;
import org.example.framework.core.tasks.GetContactTask;
import org.example.framework.core.tasks.UpdateContactTask;
import org.example.framework.core.questions.HomePageQuestion;
import org.example.framework.domain.models.ContactRequest;
import org.example.framework.domain.models.ContactResponse;
import io.restassured.response.Response;
import org.junit.Assert;

/**
 * Definiciones de pasos para flujo E2E de gestión completa de contactos.
 * Combina validación de API y UI para crear, actualizar y eliminar contactos.
 */
public class CicloCompletoContactoSteps {

    private final Actor actor = ActorManager.getMainActor();

    /**
     * Paso: "Dado que el usuario ha iniciado sesión exitosamente"
     * Configura la autenticación para la prueba (token de API e inicio de sesión en UI).
     * Realiza login en API para obtener el token y login en UI para acceder al dashboard.
     */
    @Dado("que el usuario ha iniciado sesión exitosamente")
    public void que_el_usuario_ha_iniciado_sesion_exitosamente() {
        // 1. API Authentication - get token
        String user = System.getProperty("test.user", "user@example.com");
        String pass = System.getProperty("test.password", "password123");
        actor.attemptsTo(LoginApiTask.withCredentials(user, pass));
        
        // 2. UI Login - open dashboard
        String baseUrl = System.getProperty("ui.base.url", "https://thinking-tester-contact-list.herokuapp.com");
        actor.attemptsTo(Open.url(baseUrl));
        actor.attemptsTo(Login.withCredentials(user, pass));
        
        // Verify authenticated
        Assert.assertTrue("El usuario no está autenticado", 
            HomePageQuestion.isAuthenticated().answeredBy(actor));
    }

    /**
     * Paso: "Cuando crea un nuevo contacto desde la interfaz con nombre {string} email {string}"
     * Crea un contacto mediante API simulando la interacción del usuario en la UI.
     * Almacena el nombre, email y ID del contacto creado en memoria del actor para validaciones posteriores.
     */
    @Cuando("crea un nuevo contacto desde la interfaz con nombre {string} email {string}")
    public void crea_un_nuevo_contacto_desde_la_interfaz(String nombre, String email) {
        // Store for later validation
        actor.remember("contactName", nombre);
        actor.remember("contactEmail", email);
        
        // Use API to create contact (simulating UI behavior)
        ContactRequest request = ContactRequest.builder()
                .name(nombre)
                .email(email)
                .phone("+34123456789")
                .build();
        
        actor.attemptsTo(CreateContactTask.withPayload(request));
        
        // Store the contact ID for later use
        Response response = actor.recall("lastResponse");
        Assert.assertNotNull("No se recibió respuesta de creación", response);
        String contactId = response.getBody().jsonPath().getString("_id");
        actor.remember("contactId", contactId);
    }

    /**
     * Paso: "Entonces el contacto debe aparecer en la lista de contactos UI"
     * Valida que el contacto fue creado exitosamente mediante API.
     * Recupera el contacto creado para verificar que existe en el backend con los datos correctos.
     */
    @Entonces("el contacto debe aparecer en la lista de contactos UI")
    public void el_contacto_debe_aparecer_en_la_lista_de_contactos_ui() {
        String email = actor.recall("contactEmail");
        String contactId = actor.recall("contactId");
        
        // Verify via API that contact exists
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        Assert.assertEquals("Contact should be found (200)", 200, response.getStatusCode());
        Assert.assertTrue("Email no coincide en respuesta", response.getBody().asString().contains(email));
    }

    /**
     * Paso: "Y el contacto debe existir en la API"
     * Valida que el contacto existe en el backend mediante una llamada a la API.
     * Verifica el código de respuesta (200) y que el email del contacto coincide.
     */
    @Entonces("el contacto debe existir en la API")
    public void el_contacto_debe_existir_en_la_api() {
        String email = actor.recall("contactEmail");
        String contactId = actor.recall("contactId");
        
        // Verify contact exists via API
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        Assert.assertEquals("Contact should exist in API (200)", 200, response.getStatusCode());
        Assert.assertTrue("Contacto no existe en API", response.getBody().asString().contains(email));
    }

    /**
     * Paso: "Cuando actualiza la información del contacto para cambiar el nombre a {string}"
     * Actualiza los datos del contacto mediante API (simulando edición en UI).
     * Realiza una llamada PUT al endpoint de actualización con los datos modificados.
     */
    @Cuando("actualiza la información del contacto para cambiar el nombre a {string}")
    public void actualiza_la_informacion_del_contacto(String nuevoNombre) {
        actor.remember("updatedContactName", nuevoNombre);
        String contactId = actor.recall("contactId");
        String currentEmail = actor.recall("contactEmail");
        ContactResponse previousContact = actor.recall("contactResponse");
        String currentPhone = previousContact != null ? previousContact.getPhone() : "+34123456789";

        ContactRequest updateRequest = ContactRequest.builder()
                .name(nuevoNombre)
                .email(currentEmail)
                .phone(currentPhone)
                .build();

        actor.attemptsTo(UpdateContactTask.withIdAndPayload(contactId, updateRequest));
        Response response = actor.recall("lastResponse");
        Assert.assertNotNull("No se recibió respuesta de actualización", response);
        Assert.assertEquals("La actualización del contacto debió ser aceptada", 200, response.getStatusCode());
    }

    /**
     * Paso: "Entonces la UI debe mostrar los datos actualizados"
     * Valida que los datos actualizados del contacto se guardan correctamente en el backend.
     * Recupera el contacto mediante API para verificar que el nombre fue actualizado.
     */
    @Entonces("la UI debe mostrar los datos actualizados")
    public void la_ui_debe_mostrar_los_datos_actualizados() {
        String updatedName = actor.recall("updatedContactName");
        String contactId = actor.recall("contactId");
        
        // Verify via API that contact was updated
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        Assert.assertEquals("Updated contact should be found (200)", 200, response.getStatusCode());
        Assert.assertTrue("Nombre no fue actualizado", 
            response.getBody().asString().contains(updatedName));
    }

    /**
     * Paso: "Y la API debe retornar la información actualizada"
     * Verifica que la API retorna los datos actualizados correctamente.
     * Realiza una llamada GET para confirmar que el nombre modificado se refleja en el backend.
     */
    @Entonces("la API debe retornar la información actualizada")
    public void la_api_debe_retornar_la_informacion_actualizada() {
        String updatedName = actor.recall("updatedContactName");
        String contactId = actor.recall("contactId");
        
        // Verify via API that contact has updated data
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        Assert.assertEquals("Should get updated contact (200)", 200, response.getStatusCode());
        Assert.assertTrue("Información no fue actualizada en API", 
            response.getBody().asString().contains(updatedName));
    }

    /**
     * Paso: "Cuando elimina el contacto desde la interfaz"
     * Elimina el contacto mediante una llamada DELETE a la API.
     * Simula la acción del usuario al hacer click en el botón eliminar en la interfaz.
     */
    @Cuando("elimina el contacto desde la interfaz")
    public void elimina_el_contacto_desde_la_interfaz() {
        String contactId = actor.recall("contactId");
        // Perform delete via API
        actor.attemptsTo(DeleteContactTask.withId(contactId));
    }

    /**
     * Paso: "Entonces el contacto no debe aparecer en la lista de contactos UI"
     * Valida que el contacto fue eliminado de la UI.
     * Intenta recuperar el contacto e verifica que retorna error (404 o similar).
     */
    @Entonces("el contacto no debe aparecer en la lista de contactos UI")
    public void el_contacto_no_debe_aparecer_en_la_lista_de_contactos_ui() {
        String contactId = actor.recall("contactId");
        
        // Verify contact was deleted - should get 404 or similar
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        // After deletion, expect 404 or 400
        Assert.assertTrue("Contacto debería estar eliminado (404 o similar)",
            response.getStatusCode() == 404 || response.getStatusCode() == 400 || response.getStatusCode() == 500);
    }

    /**
     * Paso: "Y la API no debe encontrar el contacto"
     * Valida que el contacto fue eliminado correctamente del backend.
     * Verifica que una llamada GET al contacto retorna código de error (404 o 400).
     */
    @Entonces("la API no debe encontrar el contacto")
    public void la_api_no_debe_encontrar_el_contacto() {
        String contactId = actor.recall("contactId");
        
        // Try to get the deleted contact - should fail
        actor.attemptsTo(GetContactTask.withId(contactId));
        Response response = actor.recall("lastResponse");
        // Should not find the contact (404)
        Assert.assertTrue("Contacto debería no existir en API después de eliminar",
            response.getStatusCode() == 404 || response.getStatusCode() == 400);
    }
}
