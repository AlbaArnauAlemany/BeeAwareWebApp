package ch.unil.doplab.beeaware;

import ch.unil.doplab.beeaware.Domain.PollenLocationIndex;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import lombok.var;

import java.util.Map;

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

    public Map<Long, PollenLocationIndex> getPollenLocationIndex() {
        try {
            return serviceTarget
                    .path("pollenlocationindex")
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<Map<Long, PollenLocationIndex>>(){});
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't retrieve the pollen location index: " + e.getMessage());
            return null;
        }
    }

}
