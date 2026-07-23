package za.ac.belgiumcampus.cleaninginventory.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application configuration from classpath properties files.
 */
public class AppConfig {

    private static final Properties PROPERTIES = new Properties();
    private static boolean loaded = false;

    private AppConfig() {
    }

    public static synchronized void load() {
        if (loaded) {
            return;
        }

        try (InputStream input = AppConfig.class.getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                        "database.properties not found. Copy database.properties.example to "
                                + "database.properties and fill in your Supabase credentials.");
            }
            PROPERTIES.load(input);
            loaded = true;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load database.properties", e);
        }
    }

    public static String get(String key) {
        load();
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required property: " + key);
        }
        return value.trim();
    }

    public static String getDbUrl() {
        return get("db.url");
    }

    public static String getDbUsername() {
        return get("db.username");
    }

    public static String getDbPassword() {
        return get("db.password");
    }
}
