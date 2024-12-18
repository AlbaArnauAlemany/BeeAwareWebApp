package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.Coordinate;
import ch.unil.doplab.beeaware.Domain.DTO.AllergenDTO;
import ch.unil.doplab.beeaware.Domain.DTO.BeezzerDTO;
import ch.unil.doplab.beeaware.Domain.DTO.LocationDTO;
import ch.unil.doplab.beeaware.Domain.Beezzer;
import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import ch.unil.doplab.beeaware.Domain.Pollen;
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
        return beezzerTarget
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<>() {
                });
    }

    public BeezzerDTO getBeezzer(Long id) {
        try {
            System.out.println("Sending request to get Beezzer with ID: " + id);
            return beezzerTarget
                    .path(id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .get(BeezzerDTO.class);
        } catch (Exception e) {
            System.out.println("Error retrieving Beezzer: " + e.getMessage());
            return null;
        }
    }

    // TODO: Changer le set pour modifier uniquement les éléments updatés et prendre un json
    public boolean setBeezzer(Long id, Beezzer beezzer) throws Exception {
        var response = beezzerTarget
                    .path("set")
                    .path(id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(beezzer, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getStatus() == 200;
        } else {
            throw new Exception("Sorry, we couldn't set the beezzer. Status: " + response.getStatus());
        }
    }

    public boolean checkBeezzerUsername(String beezzerUsername){
        try {

            WebTarget targetWithParams = beezzerTarget
                    .path("username")
                    .queryParam("username", beezzerUsername);

            Response response = targetWithParams
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(null));

            if (response.getStatus() == 200) {
                return response.readEntity(Boolean.class);
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't add the allergen: " + e.getMessage());
            return false;
        }
    }

    public BeezzerDTO addBeezzer(Beezzer beezzer) {
        try {
            return beezzerTarget
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(beezzer, MediaType.APPLICATION_JSON), BeezzerDTO.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't create the beezzer: " + e.getMessage());
            return null;
        }
    }

    public boolean removeBeezzer(Long id) throws Exception {
        var response = beezzerTarget
                    .path(id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .delete();
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                return response.getStatus() == 200;
            } else {
                    throw new Exception("Sorry, we couldn't delete the beezzer. Status: " + response.getStatus());
            }
    }

    public LocationDTO getBeezzerLocation(Long id) {
        try {
            return beezzerTarget
                    .path("locations" + id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .get(LocationDTO.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't retrieve the beezzer's location: " + e.getMessage());
            return null;
        }
    }

    public boolean setBeezzerLocation(Long id, LocationDTO location) {
        var response = beezzerTarget
                    .path("location").path(id.toString())
                    .request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(location, MediaType.APPLICATION_JSON));
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getStatus() == 200;
        } else {
            return false;
        }
    }

    public AllergenDTO getBeezzerAllergen(Long id) {
        try {
            return beezzerTarget
                    .path(id.toString()).path("allergens")
                    .request(MediaType.APPLICATION_JSON)
                    .get(AllergenDTO.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't retrieve the allergen: " + e.getMessage());
            return null;
        }
    }

    public boolean addAllergen(Long id, String pollen) {
        try {
            return beezzerTarget
                    .path(id.toString()).path("allergens")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(pollen, MediaType.APPLICATION_JSON), Boolean.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't add the allergen: " + e.getMessage());
            return false;
        }
    }

    public boolean addAllergenSet(Long id, String pollen) {
        try {
            return beezzerTarget
                    .path(id.toString()).path("allergens")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(pollen, MediaType.APPLICATION_JSON), Boolean.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't add the allergen set: " + e.getMessage());
            return false;
        }
    }

    public boolean setAllergenSet(Long id, List<Pollen> pollen) {
        try {
            return beezzerTarget
                    .path(id.toString()).path("setallergensset")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(pollen, MediaType.APPLICATION_JSON), Boolean.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't add the allergen set: " + e.getMessage());
            return false;
        }
    }

    public boolean changePassword(Long id, String password) {
        try {
            WebTarget targetWithParams = beezzerTarget
                    .path(id.toString())
                    .path("password")
                    .queryParam("password", password);

            Response response = targetWithParams
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(null));

            if (response.getStatus() == 200) {
                return response.readEntity(Boolean.class);
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't change the password: " + e.getMessage());
            return false;
        }
    }

    public boolean removeAllergen(Long id, String pollen) throws Exception{
        var response = beezzerTarget
                .path(id.toString()).path("allergens")
                .request(MediaType.APPLICATION_JSON)
                .delete();
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.getStatus() == 200;
        } else {
            throw new Exception("Sorry, we couldn't delete the allergen. Status: " + response.getStatus());
        }
    }
}
