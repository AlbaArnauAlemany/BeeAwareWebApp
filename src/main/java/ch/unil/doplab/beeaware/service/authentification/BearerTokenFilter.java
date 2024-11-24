package ch.unil.doplab.beeaware.service.authentification;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.net.http.HttpRequest;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BearerTokenFilter implements ClientRequestFilter {

    HttpSession session;

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        HttpSession session = SessionUtils.getSession(false); // Récupère la session globale
        if (session == null) {
            System.out.println("No active session found in BearerTokenFilter.");
            return;
        }

        System.out.println("Session ID in BearerTokenFilter: " + session.getId());
        System.out.println("Session attributes:");
        session.getAttributeNames().asIterator().forEachRemaining(attr -> {
            System.out.println(attr + " : " + session.getAttribute(attr));
        });

        String bearerToken = (String) session.getAttribute("bearerToken");
        System.out.println("BearerTokenFilter invoked for URL: " + requestContext.getUri());

        if (bearerToken != null) {
            requestContext.getHeaders().add("Authorization", "Bearer " + bearerToken);
            System.out.println("Bearer Token sent: " + bearerToken);
        } else {
            System.out.println("No Bearer Token found in session.");
        }
    }

}



