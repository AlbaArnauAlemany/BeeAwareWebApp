package ch.unil.doplab.beeaware.ui;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Named
@SessionScoped
@Getter
@Setter
public class RegisterBean implements Serializable {
    private String username;
    private String password;
    private String email;

    // Méthode d'enregistrement
    public String register() {
        //TODO: Logique d'enregistrement ici (base de données, validations, etc.)
        System.out.println("User registered: " + username + ", " + email);

        // Redirige vers la page de résumé
        return "summary.xhtml";
    }
}
