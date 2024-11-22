package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.PollenLocationIndex;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class ServiceResource {
    private WebTarget serviceTarget;

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
