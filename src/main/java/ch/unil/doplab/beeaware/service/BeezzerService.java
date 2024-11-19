package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.Beezzer;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class BeezzerService {
    private WebTarget beezzerTarget;


    public List<BeezzerDTO> getAllBeezzers() {
        var beezzers = beezzerTarget
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<BeezzerDTO>>() {
                });
        return beezzers;
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
