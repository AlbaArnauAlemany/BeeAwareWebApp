package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Symptom;
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
                .path("create/" + beezzerId.toString())
                .queryParam("reaction", reactionValue)
                .queryParam("antihistamine", antihistamine);

        try (Response response = targetWithParams
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(null))) {

            if (response.getStatus() == 200) {
                return response.readEntity(Symptom.class);
            } else {
                throw new RuntimeException("Sorry, we couldn't register the symptom: " + response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception e) {
            // Handle the exception if needed (e.g., logging the error)
            throw new RuntimeException("Error occurred while creating symptom: " + e.getMessage(), e);
        }
    }

    public SymptomsDTO getSymptomForDate(Long beezzerId, String date) {
        WebTarget targetWithParams = symptomTarget
                .path("{id}/date")
                .resolveTemplate("id", beezzerId)
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