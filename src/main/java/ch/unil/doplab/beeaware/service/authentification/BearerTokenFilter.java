package ch.unil.doplab.beeaware.service.authentification;

import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class BearerTokenFilter implements ClientRequestFilter {

    private HttpSession session;

    public BearerTokenFilter(HttpSession session) {
        this.session = session;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        if (session != null) {
            String token = (String) session.getAttribute("bearerToken");
            if (token != null) {
                requestContext.getHeaders().add("Authorization", "Bearer " + token);
            }
        }
    }
}


