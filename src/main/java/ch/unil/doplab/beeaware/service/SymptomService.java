package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Symptom;
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
public class SymptomService {

    private WebTarget symptomTarget;

    public boolean addSymptom(Symptom symptom) {
        try {
            return symptomTarget
                    .path("add")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(symptom, MediaType.APPLICATION_JSON), Boolean.class);
        } catch (Exception e) {
            System.out.println("Sorry, we couldn't add the symptom: " + e.getMessage());
            return false;
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

    public List<SymptomsDTO> getSymptomForRangeDate(Long beezzerId, String dateFrom, String dateTo) {
        WebTarget targetWithParams = symptomTarget
                .path("{id}/date")
                .resolveTemplate("id", beezzerId)
                .path(dateFrom)
                .path(dateTo);

        return targetWithParams
                .request(MediaType.APPLICATION_JSON)
                .get(new GenericType<List<SymptomsDTO>>() {});
    }
}