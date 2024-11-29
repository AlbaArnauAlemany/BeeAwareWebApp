package ch.unil.doplab.beeaware.service;


import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class IndexPollenForBeezzerService {

    private WebTarget indexPollenForBeezzerTarget;

    public List<PollenInfoDTO> getIndexForDate(String date, Long beezzerId) {
        WebTarget targetWithParams = indexPollenForBeezzerTarget
                .path("date/beezzer/" + beezzerId.toString())
                .queryParam("date", date);

        Response response = targetWithParams
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<PollenInfoDTO>>() {});
        } else if (response.getStatus() == 404) {
        return Collections.emptyList();
        } else {
            throw new RuntimeException("[We are in the IndexPollenForBeezzerService class] Sorry, we couldn't retrieve the pollen indexes for the beezzer :" + response.getStatusInfo().getReasonPhrase());
        }
    }
}
