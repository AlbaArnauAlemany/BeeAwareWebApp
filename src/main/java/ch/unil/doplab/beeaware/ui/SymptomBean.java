package ch.unil.doplab.beeaware.ui;

import ch.unil.doplab.beeaware.ApplicationServiceManagement;
import ch.unil.doplab.beeaware.DTO.SymptomsDTO;
import ch.unil.doplab.beeaware.Domain.Reaction;
import ch.unil.doplab.beeaware.Domain.Symptom;
import ch.unil.doplab.beeaware.Domain.Token;
import ch.unil.doplab.beeaware.service.BeezzerService;
import ch.unil.doplab.beeaware.service.SymptomService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Named
@Getter
@Setter
public class SymptomBean {

    @Inject
    ApplicationServiceManagement theService;

    @Inject
    BeezzerData beezzerData;


    private String date;
    private Long beezzerId;
    private int reactionValue;
    private boolean antihistamine;
    private Symptom symptomInfo;

    @PostConstruct
    public void init() {
        beezzerId = beezzerData.getId();
    }

    public void reset() {
        this.date = null;
        this.beezzerId = null;
        this.reactionValue = 0;
        this.antihistamine = false;
    }

    public void setReactionValue(int reactionValue) {
        if (reactionValue < 0 || reactionValue > 5) {
            throw new IllegalArgumentException("Reaction value must be between 0 and 5.");
        }
        this.reactionValue = reactionValue;
    }

    public Symptom registerSymptoms(int reactionValue, boolean antihistamine, Long beezzerId) {
        symptomInfo = theService.getSymptomService().createSymptom(reactionValue, antihistamine, beezzerId);
        return symptomInfo;
    }

}
