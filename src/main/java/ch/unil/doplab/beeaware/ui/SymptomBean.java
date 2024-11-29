package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Reaction;
import ch.unil.doplab.beeaware.Domain.Symptom;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Named
@Getter
@Setter
public class SymptomBean {

    @Inject
    BeezzerData beezzerData;

    private String date;
    private Long beezzerId;
    private int reactionValue;
    private boolean antihistamine;
    private String reactionName;
    private SymptomsDTO symptomInfo;
    private Symptom registeredSymptoms;

    @PostConstruct
    public void init() {

        updateReactionName();

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

        symptomInfo = getSymptomForDate(beezzerId, date);
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.reactionValue = 0;
        this.antihistamine = false;
    }

    public void updateReactionName() {
        reactionName = Reaction.fromValue(reactionValue).toString();
    }

    public void setReactionValue(int reactionValue) {
        if (reactionValue < 0 || reactionValue > 5) {
            throw new IllegalArgumentException("Reaction value must be between 0 and 5.");
        }
        this.reactionValue = reactionValue;
    }

    public Symptom registerSymptoms() {
        try {
            registeredSymptoms = beezzerData.getTheService().getSymptomService().createSymptom(reactionValue, antihistamine, beezzerId);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Symptoms registered successfully!"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to register symptoms."));
        }
        return null;
    }

    public SymptomsDTO getSymptomForDate(Long beezzerId, String date) {
        try {
            System.out.println("Beezzer ID: " + beezzerId);
            System.out.println("Date: " + date);
            symptomInfo = beezzerData.getTheService().getSymptomService().getSymptomForDate(beezzerId, date);
            return symptomInfo;
        } catch (RuntimeException e) {
            System.out.println("[We are in the SymptomBean class] The getSymptomForDate() methode couldn't be handled...");
            e.printStackTrace();
            return null;
        }
    }
}
