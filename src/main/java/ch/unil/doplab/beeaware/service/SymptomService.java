package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Symptom;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SymptomService {

    private WebTarget symptomTarget;

    public Symptom createSymptom(int reactionValue, boolean antihistamine, Long beezzerId) {
        WebTarget targetWithParams = symptomTarget
                .path("create")
                .queryParam("reactionValue", reactionValue)
                .queryParam("antihistamine", antihistamine)
                .queryParam("beezzerId", beezzerId);

        Response response = targetWithParams
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(null));

        if (response.getStatus() == 200) {
            return response.readEntity(Symptom.class);
        } else {
            throw new RuntimeException("Sorry, we couldn't register the symptom: " + response.getStatusInfo().getReasonPhrase());
        }
    }

    public SymptomsDTO getSymptomForDate(Long beezzerId, String date) {
        WebTarget targetWithParams = symptomTarget
                .path(beezzerId.toString() + "/date")
                .queryParam("date", date);

        Response response = targetWithParams
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(SymptomsDTO.class);
        } else {
            throw new RuntimeException("Sorry, we couldn't fetch the symptoms: " + response.getStatusInfo().getReasonPhrase());
        }
    }
}