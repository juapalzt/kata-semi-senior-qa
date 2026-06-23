package org.example.tests.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestDataManager {
    
    private static final String TESTDATA_PATH = "src/test/resources/testdata/testdata.json";
    private static final String PROPERTIES_PATH = "src/test/resources/environments/dev/properties/testdata.properties";
    private JsonObject testDataJson;
    private Properties properties;

    public TestDataManager() {
        loadJsonTestData();
        loadProperties();
    }

    /**
     * OPCIÓN 1: Obtener datos desde properties por ambiente
     * 
     * Uso en Steps:
     *   String email = testDataManager.getPropertyValue("test.user.email");
     *   String password = testDataManager.getPropertyValue("test.user.password");
     */
    public String getPropertyValue(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Propiedad no encontrada: " + key);
        }
        return value;
    }

    /**
     * OPCIÓN 2: Obtener datos desde JSON
     * 
     * Uso en Steps:
     *   User user = testDataManager.getUserByIndex(0);
     *   Contact contact = testDataManager.getContactByIndex(0);
     */
    public User getUserByIndex(int index) {
        JsonObject userJson = testDataJson.getAsJsonArray("users")
                .get(index).getAsJsonObject();
        return new User(
            userJson.get("name").getAsString(),
            userJson.get("lastname").getAsString(),
            userJson.get("email").getAsString(),
            userJson.get("password").getAsString()
        );
    }

    public Contact getContactByIndex(int index) {
        JsonObject contactJson = testDataJson.getAsJsonArray("contacts")
                .get(index).getAsJsonObject();
        return new Contact(
            contactJson.get("name").getAsString(),
            contactJson.get("lastname").getAsString(),
            contactJson.get("email").getAsString(),
            contactJson.get("phone").getAsString(),
            contactJson.get("city").getAsString(),
            contactJson.get("country").getAsString()
        );
    }

    public InvalidCredentials getInvalidCredentials() {
        JsonObject invalidJson = testDataJson.getAsJsonObject("invalidCredentials");
        return new InvalidCredentials(
            invalidJson.get("email").getAsString(),
            invalidJson.get("password").getAsString()
        );
    }

    private void loadJsonTestData() {
        try {
            String content = Files.readString(Paths.get(TESTDATA_PATH));
            this.testDataJson = JsonParser.parseString(content).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException("Error cargando testdata.json: " + e.getMessage());
        }
    }

    private void loadProperties() {
        properties = new Properties();
        try {
            InputStream input = Files.newInputStream(Paths.get(PROPERTIES_PATH));
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando testdata.properties: " + e.getMessage());
        }
    }

    // Clases internas para mapeo de datos
    public static class User {
        public String name;
        public String lastname;
        public String email;
        public String password;

        public User(String name, String lastname, String email, String password) {
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.password = password;
        }
    }

    public static class Contact {
        public String name;
        public String lastname;
        public String email;
        public String phone;
        public String city;
        public String country;

        public Contact(String name, String lastname, String email, String phone, String city, String country) {
            this.name = name;
            this.lastname = lastname;
            this.email = email;
            this.phone = phone;
            this.city = city;
            this.country = country;
        }
    }

    public static class InvalidCredentials {
        public String email;
        public String password;

        public InvalidCredentials(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
