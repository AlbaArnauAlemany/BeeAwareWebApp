package ch.unil.doplab.beeaware;

import ch.unil.doplab.beeaware.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.Beezzer;
import ch.unil.doplab.beeaware.DTO.AllergenDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
                    .get(new GenericType<List<BeezzerDTO>>() {});
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

    // TODO: Changer le set pour modifier uniquement les éléments updatés et prendre in json
    public boolean setBeezzer(Beezzer beezzer) throws Exception{
        var response = beezzerTarget
                    .path("setBeezzer")
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(beezzer, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getStatus() == 200;
        } else {
            throw new Exception("Failed to set Beezzer. Status: " + response.getStatus());
        }
    }
}
