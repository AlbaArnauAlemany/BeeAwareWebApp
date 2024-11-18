package ch.unil.doplab.beeaware;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;

@ApplicationScoped
public class Service {
    private static final String BASE_URL = "http://localhost:8080/BeeAwareService-1.0-SNAPSHOT/api";
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
        System.out.println("BeeAwareService" + this.hashCode());
        Client client = ClientBuilder.newClient();
        serviceTarget = client.target(BASE_URL).path("service");
    }

    public void resetService() {
        String response = serviceTarget
                .path("reset")
                .request()
                .get(String.class);
    }

    public void getPollenLocationIndex() {
        String response = serviceTarget
                .path("pollenlocationindex")
                .request()
                .get(String.class);
    }

}
