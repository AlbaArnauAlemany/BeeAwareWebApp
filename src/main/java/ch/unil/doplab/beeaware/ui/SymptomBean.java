package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.Domain.DTO.PollenInfoDTO;
import ch.unil.doplab.beeaware.Domain.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Symptom;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.*;
import org.primefaces.event.SlideEndEvent;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@ViewScoped
@Named("symptomBean")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymptomBean implements Serializable {

    @Inject
    BeezzerData beezzerData;
    private String date;
    private Long beezzerId;
    private int reactionValue = 0;
    private boolean antihistamine;
    private String reactionName = "No Reaction";
    private SymptomsDTO symptomInfo;
    private Symptom registeredSymptoms;

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct called for IndexPollenForBeezzerBean");
        System.out.println("Initialized with Reaction Value: " + reactionValue);
        System.out.println("Initialized with Reaction Name: " + reactionName);

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
        System.out.println("symptomInfo is " + (symptomInfo == null ? "null" : "not null"));
        System.out.println("SymptomInfo post construct: "+ symptomInfo);
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.reactionValue = 0;
        this.reactionName = null;
        this.antihistamine = false;
    }

    public void updateReactionName() {
        System.out.println("Inside updateReactionName method...");
        if (FacesContext.getCurrentInstance().getExternalContext().getRequestMap().containsKey("symptomBean")) {
            System.out.println("SymptomBean exists!");
        } else {
            System.out.println("SymptomBean is NULL!");
        }
        System.out.println("Slider updated! Reaction Value: " + reactionValue);
        switch (reactionValue) {
            case 0: reactionName = "No Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "no allergic reaction today!"));
                break;
            case 1: reactionName = "Mild Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "a mild allergic reaction today!"));
                break;
            case 2: reactionName = "Moderate Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "a moderate allergic reaction today!"));
                break;
            case 3: reactionName = "Strong Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "a strong allergic reaction today!"));
                break;
            case 4: reactionName = "Severe Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "a sever allergic reaction today!"));
                break;
            case 5: reactionName = "Very Severe Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "You are feeling", "a very severe allergic reaction today!"));
                break;
            default: reactionName = "Unknown Reaction";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reaction not loaded properly", "How are you feeling today?"));
        }
        System.out.println("Updated Reaction Name: " + reactionName);
    }

    public boolean registerSymptoms() {
        try {
            registeredSymptoms = new Symptom(beezzerId, reactionValue, antihistamine, new Date());
            boolean set = beezzerData.getTheService().getSymptomService().addSymptom(registeredSymptoms);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Symptoms registered successfully!"));
            symptomInfo = getSymptomForDate(beezzerId, date);
            return set;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to register symptoms."));
            return false;
        }
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

    public String getSymptomClass() {
        int index = symptomInfo.getReaction();
        if (index == 0) return "none";
        if (index == 1) return "very-low";
        if (index == 2) return "low";
        if (index == 3) return "medium";
        if (index == 4) return "high";
        return "very-high";
    }
}
