package ch.unil.doplab.beeaware.service.authentification;

import ch.unil.doplab.beeaware.Domain.Token;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthentificationService {

    private WebTarget serviceTarget;

    public boolean logout(Long id) {
        try {
            WebTarget targetWithPathParam = serviceTarget.path(String.valueOf(id));

            System.out.println("Logout URL: " + targetWithPathParam.getUri());

            return targetWithPathParam
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(null), Boolean.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't logout" + e.getMessage());
            return false;
        }
    }

    public Token authenticate(String username, String password) {
        try {
            String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8.toString());

            // Construire le corps encodé
            String formBody = "username=" + encodedUsername + "&password=" + encodedPassword;

            // Construire le client HTTP
            Client client = ClientBuilder.newClient();

            // Envoyer la requête POST
            Response response = client.target("http://localhost:8080/BeeAwareService-1.0-SNAPSHOT/api/authentication")
                    .request(MediaType.APPLICATION_JSON) // Attend une réponse JSON
                    .post(Entity.entity(formBody, MediaType.APPLICATION_FORM_URLENCODED)); // Envoi des données encodées

            if (response.getStatus() == 200) {
                return response.readEntity(Token.class); // Lire la réponse comme une chaîne
            } else {
                throw new RuntimeException("Authentication failed: " + response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error encoding parameters: " + e.getMessage(), e);
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    static class AuthRequest {
        private String username;
        private String password;
    }
}
