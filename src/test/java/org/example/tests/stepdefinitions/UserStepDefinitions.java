package org.example.tests.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.tests.utils.TestDataManager;

public class UserStepDefinitions {
    
    private TestDataManager testDataManager;

    // Inicialización
    public UserStepDefinitions() {
        this.testDataManager = new TestDataManager();
    }

    /**
     * OPCIÓN 1: Usar propiedades (Properties)
     * 
     * Feature:
     *   Cuando crea un usuario mediante API
     */
    @When("crea un usuario mediante API")
    public void creaUsuarioPorAPI() {
        // Obtener datos desde archivo de propiedades
        String email = testDataManager.getPropertyValue("test.user.email");
        String password = testDataManager.getPropertyValue("test.user.password");
        String name = testDataManager.getPropertyValue("test.user.name");

        System.out.println("Creando usuario:");
        System.out.println("  Email: " + email);
        System.out.println("  Password: " + password);
        System.out.println("  Name: " + name);
        
        // API call usando los datos externalizados
        // apiClient.createUser(email, password, name);
    }

    /**
     * OPCIÓN 2: Usar JSON con índices
     * 
     * Feature:
     *   Dado que existe un usuario registrado
     */
    @Given("que existe un usuario registrado")
    public void existeUsuarioRegistrado() {
        TestDataManager.User user = testDataManager.getUserByIndex(0);
        
        System.out.println("Usando usuario:");
        System.out.println("  Email: " + user.email);
        System.out.println("  Password: " + user.password);
        System.out.println("  Name: " + user.name);
        
        // Crear usuario via API
        // apiClient.createUser(user.email, user.password, user.name);
    }

    /**
     * OPCIÓN 3: Combinar Properties + Generación dinámica
     * 
     * Feature:
     *   Dado que existe información válida de usuario
     */
    @Given("que existe información válida de usuario")
    public void existeInformacionValida() {
        // Usar properties como base y generar datos únicos
        String baseEmail = testDataManager.getPropertyValue("test.user.email");
        String password = testDataManager.getPropertyValue("test.user.password");
        
        // Generar email único agregando timestamp
        String uniqueEmail = baseEmail.replace("@", "+" + System.currentTimeMillis() + "@");
        
        System.out.println("Email único generado: " + uniqueEmail);
        System.out.println("Password: " + password);
    }

    /**
     * OPCIÓN 4: Usar datos inválidos para casos negativos
     * 
     * Feature:
     *   Cuando intenta iniciar sesión con contraseña incorrecta
     */
    @When("intenta iniciar sesión con contraseña incorrecta")
    public void intentaLoginConContraseñaIncorrecta() {
        TestDataManager.InvalidCredentials invalidCreds = testDataManager.getInvalidCredentials();
        
        System.out.println("Intentando login con:");
        System.out.println("  Email: " + invalidCreds.email);
        System.out.println("  Password: " + invalidCreds.password);
        
        // apiClient.login(invalidCreds.email, invalidCreds.password);
    }
}
