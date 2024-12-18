package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.Coordinate;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
public class CoordinateService {

    private WebTarget coordinateTarget;

    public Coordinate getCoordinates(int npa, String country) {
        WebTarget targetWithParams = coordinateTarget
                .queryParam("npa", npa)               // Paramètre NPA
                .queryParam("country", country);
        Response response = targetWithParams
                .request(MediaType.APPLICATION_JSON)  // Type de réponse attendu
                .post(Entity.json(null));

        if (response.getStatus() == 200) {
            return response.readEntity(Coordinate.class);
        } else {
            throw new RuntimeException("Authentication failed: " + response.getStatusInfo().getReasonPhrase());
        }
    }
}
