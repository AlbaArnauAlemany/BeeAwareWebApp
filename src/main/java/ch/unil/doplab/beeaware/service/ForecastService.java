package ch.unil.doplab.beeaware.service;

import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import jakarta.ws.rs.client.WebTarget;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


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
    public class ForecastService {

        private WebTarget forecastTarget;

        public List<PollenInfoDTO> getForecastForRangeDate(Long beezzerId, String dateFrom, String dateTo) {
            WebTarget targetWithParams = forecastTarget
                    .path("{id}/date")
                    .resolveTemplate("id", beezzerId)
                    .path(dateFrom)
                    .path(dateTo);

            return targetWithParams
                    .request(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<PollenInfoDTO>>() {});
        }
}
