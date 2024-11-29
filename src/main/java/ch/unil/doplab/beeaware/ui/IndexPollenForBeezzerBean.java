package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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
    // private static final long serialVersionUID = 1L;

    @Inject
    BeezzerData beezzerData;

    private String date;
    private Long beezzerId;
    private List<PollenInfoDTO> pollenInfo;
    private boolean noIndexCharge;

    @PostConstruct
    public void init() {
        noIndexCharge = false;

        // Check for not null injects
        if (beezzerData == null) {
            throw new RuntimeException("BeezzerData is not injected!");
        }
        if (beezzerData.getTheService() == null) {
            throw new RuntimeException("ApplicationServiceManagement is not injected into BeezzerData!");
        }

        // Retrieve beezzer id from beezzerData
        beezzerId = beezzerData.getId();

        // Use today's date and correctly format it for future use
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        date = LocalDate.now().format(formatter);

        // Check correct population of id and date
        System.out.println("Beezzer ID: " + beezzerId);
        System.out.println("Date: " + date);

        // Retrieve pollen information for the beezzer today
        pollenInfo = getIndex(date, beezzerId);

        // Check if method correctly called and give debug details
        if (pollenInfo != null) {
            pollenInfo.forEach(pollen -> System.out.println(pollen.getDisplayName() + " - " + pollen.getIndex()));
        } else {
            System.out.println("PollenInfo is null");
        }
        System.out.println("PollenInfo size: " + (pollenInfo != null ? pollenInfo.size() : "null"));
        System.out.println("PostConstruct called for IndexPollenForBeezzerBean");

        // Check if no pollen charge for the beezzer and display nice message
        checkAllIndexesEmpty();
        System.out.println("[IndexPollenForBeezzerBean init()] Status of noIndexCharge: " + isNoIndexCharge());

        checkAndSetMessage();
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.pollenInfo = null;
        this.noIndexCharge = false;
    }

    public List<PollenInfoDTO> getIndex(String date, Long beezzerId){
        try {
            System.out.println("Beezzer ID: " + beezzerId);
            System.out.println("Date: " + date);
            pollenInfo = beezzerData.getTheService().getIndexPollenForBeezzerService().getIndexForDate(date, beezzerId);
            return pollenInfo;
        } catch (RuntimeException e) {
            System.out.println("[We are in the IndexForBeezzerBean class] The getIndex() methode couldn't be handled...");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void checkAllIndexesEmpty() {
        noIndexCharge = true;
        for (PollenInfoDTO pollenInfo : pollenInfo) {
            if (pollenInfo != null && pollenInfo.getIndex() != 0) {
                noIndexCharge = false;
                System.out.println("[checkAllIndexesEmpty] Not all indexes are empty.");
                break;
            }
        }
        System.out.println("[checkAllIndexesEmpty] All indexes are empty.");
    }

    public void checkAndSetMessage() {
        if (noIndexCharge) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Lucky day!",
                    "There is no pollen in the air today! Enjoy the outdoors and take a deep breath of fresh air!");
            FacesContext.getCurrentInstance().addMessage(null, message);
            System.out.println("Growl message added: " + message.getDetail());
        } else {
            System.out.println("No growl message needed.");
        }
    }

    public String getPollenClass(PollenInfoDTO pollenInfo) {
        int index = pollenInfo.getIndex();
        if (index == 0) return "none";
        if (index == 1) return "very-low";
        if (index == 2) return "low";
        if (index == 3) return "medium";
        if (index == 4) return "high";
        return "very-high";
    }
}
