package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@ViewScoped
@Named
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexPollenForBeezzerBean implements Serializable {

    @Inject
    BeezzerData beezzerData;

//    @Inject
//    ApplicationServiceManagement theService;

//    public IndexPollenForBeezzerBean() {
//        System.out.println("IndexPollenForBeezzerBean initialized!");
//    }

    private String date;
    private Long beezzerId;
    private List<PollenInfoDTO> pollenInfo;

    @PostConstruct
    public void init() {
        if (beezzerData == null) {
            throw new RuntimeException("BeezzerData is not injected!");
        }
        if (beezzerData.getTheService() == null) {
            throw new RuntimeException("ApplicationServiceManagement is not injected into BeezzerData!");
        }
        beezzerId = beezzerData.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        date = LocalDate.now().format(formatter);

        System.out.println("Beezzer ID: " + beezzerId);
        System.out.println("Date: " + date);

        pollenInfo = getIndex(date, beezzerId);

        if (pollenInfo != null) {
            pollenInfo.forEach(pollen -> System.out.println(pollen.getDisplayName() + " - " + pollen.getIndex()));
        } else {
            System.out.println("PollenInfo is null");
        }

        System.out.println("PollenInfo size: " + (pollenInfo != null ? pollenInfo.size() : "null"));
        System.out.println("PostConstruct called for IndexPollenForBeezzerBean");
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.pollenInfo = null;
    }

    public List<PollenInfoDTO> getIndex(String date, Long beezzerId){
        try {
            System.out.println("Beezzer ID: " + beezzerId);
            System.out.println("Date: " + date);
            pollenInfo = beezzerData.getTheService().getIndexPollenForBeezzerService().getIndex(date, beezzerId);
            return pollenInfo;
        } catch (RuntimeException e) {
            System.out.println("[We are in the IndexForBeezzerBean class] The getIndex() methode couldn't be handled...");
            e.printStackTrace();
            // Return an empty list or some default behavior if there is an error
            return Collections.emptyList();
        }
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
