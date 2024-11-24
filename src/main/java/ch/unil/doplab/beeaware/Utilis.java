package ch.unil.doplab.beeaware;


import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utilis {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Utilis.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("Fichier de configuration introuvable");
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Erreur lors du chargement des propriétés", ex);
        }
    }
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

}
