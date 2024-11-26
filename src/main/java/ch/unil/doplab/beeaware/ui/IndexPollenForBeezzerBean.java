package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Named
@Getter
@Setter
public class IndexPollenForBeezzerBean implements Serializable {
    @Inject
    BeezzerData beezzerData;

    private String date;
    private Long beezzerId;
    private List<PollenInfoDTO> pollenInfo;

    @PostConstruct
    public void init() {
        beezzerId = beezzerData.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        date = LocalDate.now().format(formatter);
        pollenInfo = getIndex(date, beezzerId);
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.pollenInfo = null;
    }

    public List<PollenInfoDTO> getIndex(String date, Long beezzerId){
        pollenInfo = beezzerData.theService.getIndexPollenForBeezzerService().getIndex(date, beezzerId);
        return pollenInfo;
    }

    public String getGaugeClass(int index) {
        if (index == 0) {
            return "none";
        } else if (index == 1) {
            return "very-low";
        } else if (index == 2) {
            return "low";
        } else if (index == 3) {
            return "medium";
        } else if (index == 4){
            return "high";
        } else {
            return "very-high";
        }
    }
}
