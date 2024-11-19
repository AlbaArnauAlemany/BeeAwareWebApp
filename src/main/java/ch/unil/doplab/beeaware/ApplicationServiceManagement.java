package ch.unil.doplab.beeaware;

import ch.unil.doplab.beeaware.service.ServiceRessource;
import ch.unil.doplab.beeaware.service.authentification.AuthentificationService;
import ch.unil.doplab.beeaware.service.authentification.BearerTokenFilter;
import ch.unil.doplab.beeaware.service.BeezzerService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApplicationScoped
@NoArgsConstructor
@Getter
@Setter
public class ApplicationServiceManagement {
    private final String BASE_URL = "http://localhost:8080/BeeAwareService-1.0-SNAPSHOT/api";
    private AuthentificationService authentificationService;
    private BeezzerService beezzerService;
    private ServiceRessource serviceRessource;
    private Client client;


    private WebTarget authenticationTarget;
    private WebTarget beezzerTarget;
    private WebTarget locationTarget;
    private WebTarget pollenLocationIndexTarget;
    private WebTarget indexPollenForBeezzerTarget;
    private WebTarget serviceTarget;
    private WebTarget symptomTarget;
    private WebTarget excelTarget;




    @PostConstruct
    public void init() {
        System.out.println("BeezzerService" + this.hashCode());
        client = ClientBuilder.newClient();
        authentificationService = new AuthentificationService(client.target(BASE_URL).path("authentication"));
        beezzerService = new BeezzerService(client.target(BASE_URL).path("beezzers"));
        serviceRessource = new ServiceRessource(client.target(BASE_URL).path("service"));
    }

    public void authenticateSession(HttpSession session){
        client.register(new BearerTokenFilter(session));
    }
}
