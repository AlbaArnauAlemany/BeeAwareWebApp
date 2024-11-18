package ch.unil.doplab.beeaware;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class BeezzerService {
    private static final String BASE_URL = "http://localhost:8080/StudyBuddyService-1.0-SNAPSHOT/api";
    private WebTarget beezzerTarget;

    @PostConstruct
    public void init() {
        System.out.println("BeezzerService" + this.hashCode());
        Client client = ClientBuilder.newClient();
        beezzerTarget = client.target(BASE_URL).path("beezzer");
    }

    public List<BeezzerDTO> getAllBeezzers() {
        try {
            return beezzerTarget
                    .request(MediaType.APPLICATION_JSON)
                    .get(new LinkedList<BeezzerDTO>() {
                    });
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't retrieve the beezzers list: " + e.getMessage());
            return null;
        }
    }

    public BeezzerDTO getBeezzer(Long id) {
        try {
            return beezzerTarget
                    .path(id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .get(BeezzerDTO.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't retrieve the beezzer: " + e.getMessage());
            return null;
        }
    }


}
